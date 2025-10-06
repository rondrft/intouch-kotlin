package com.example.aguirre680.data.repository

import com.example.aguirre680.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactRepository {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    init {
        loadSampleContacts()
    }

    fun addContact(contact: Contact) {
        val currentList = _contacts.value.toMutableList()
        currentList.add(contact)
        _contacts.value = currentList
    }

    fun deleteContact(contactId: String) {
        _contacts.value = _contacts.value.filter { it.id != contactId }
    }

    fun updateContact(contact: Contact) {
        val currentList = _contacts.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            currentList[index] = contact
            _contacts.value = currentList
        }
    }

    fun getContactById(contactId: String): Contact? {
        return _contacts.value.find { it.id == contactId }
    }

    fun searchContacts(query: String): List<Contact> {
        if (query.isBlank()) return _contacts.value

        return _contacts.value.filter { contact ->
            contact.firstName.contains(query, ignoreCase = true) ||
                    contact.lastName.contains(query, ignoreCase = true) ||
                    contact.phoneNumber.contains(query, ignoreCase = true)
        }
    }

    // Algunos contactos para probar
    private fun loadSampleContacts() {
        val sampleContacts = listOf(
            Contact(
                firstName = "Juan",
                lastName = "Perez",
                phoneNumber = "+54 11 1234-5678",
                email = "juan.perez@email.com",
                company = "Tech Solutions",
                isFavorite = true
            ),
            Contact(
                firstName = "Maria",
                lastName = "Garcia",
                phoneNumber = "+54 11 8765-4321",
                email = "maria.garcia@email.com",
                company = "Design Studio"
            ),
            Contact(
                firstName = "Carlos",
                lastName = "Lopez",
                phoneNumber = "+54 11 5555-6666",
                email = "carlos.lopez@email.com",
                company = "Marketing Pro"
            ),
            Contact(
                firstName = "Ana",
                lastName = "Martinez",
                phoneNumber = "+54 11 9999-0000",
                email = "ana.martinez@email.com",
                company = "Consulting Group",
                isFavorite = true
            )
        )
        _contacts.value = sampleContacts
    }

    companion object {
        @Volatile
        private var instance: ContactRepository? = null

        fun getInstance(): ContactRepository {
            return instance ?: synchronized(this) {
                instance ?: ContactRepository().also { instance = it }
            }
        }
    }
}