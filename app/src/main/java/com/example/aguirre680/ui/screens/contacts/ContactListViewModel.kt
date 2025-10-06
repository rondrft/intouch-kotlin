package com.example.aguirre680.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aguirre680.data.model.Contact
import com.example.aguirre680.data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val repository: ContactRepository = ContactRepository.getInstance()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredContacts = MutableStateFlow<List<Contact>>(emptyList())
    val filteredContacts: StateFlow<List<Contact>> = _filteredContacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Para mostrar mensajes de exito o error
    private val _snackbarMessage = MutableStateFlow<SnackbarData?>(null)
    val snackbarMessage: StateFlow<SnackbarData?> = _snackbarMessage.asStateFlow()

    init {
        observeContacts()
    }

    private fun observeContacts() {
        viewModelScope.launch {
            combine(
                repository.contacts,
                _searchQuery
            ) { contacts, query ->
                if (query.isBlank()) {
                    contacts
                } else {
                    contacts.filter { contact ->
                        contact.firstName.contains(query, ignoreCase = true) ||
                                contact.lastName.contains(query, ignoreCase = true) ||
                                contact.phoneNumber.contains(query, ignoreCase = true)
                    }
                }
            }.collect { filteredList ->
                _filteredContacts.value = filteredList
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun deleteContact(contactId: String) {
        repository.deleteContact(contactId)
    }

    fun toggleFavorite(contact: Contact) {
        val updatedContact = contact.copy(isFavorite = !contact.isFavorite)
        repository.updateContact(updatedContact)
    }

    fun showSuccessMessage(message: String) {
        _snackbarMessage.value = SnackbarData(message, SnackbarType.SUCCESS)
    }

    fun showErrorMessage(message: String) {
        _snackbarMessage.value = SnackbarData(message, SnackbarType.ERROR)
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}

data class SnackbarData(
    val message: String,
    val type: SnackbarType
)

enum class SnackbarType {
    SUCCESS,
    ERROR
}