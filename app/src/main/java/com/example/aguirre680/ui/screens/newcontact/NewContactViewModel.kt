package com.example.aguirre680.ui.screens.newcontact

import androidx.lifecycle.ViewModel
import com.example.aguirre680.data.model.Contact
import com.example.aguirre680.data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewContactViewModel(
    private val repository: ContactRepository = ContactRepository.getInstance()
) : ViewModel() {

    // Campos del formulario
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    // Manejo de errores para cada campo
    private val _firstNameError = MutableStateFlow<String?>(null)
    val firstNameError: StateFlow<String?> = _firstNameError.asStateFlow()

    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError: StateFlow<String?> = _lastNameError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow<String?>(null)
    val phoneNumberError: StateFlow<String?> = _phoneNumberError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun onFirstNameChange(value: String) {
        _firstName.value = value
        if (value.isNotBlank()) {
            _firstNameError.value = null
        }
    }

    fun onLastNameChange(value: String) {
        _lastName.value = value
        if (value.isNotBlank()) {
            _lastNameError.value = null
        }
    }

    fun onPhoneNumberChange(value: String) {
        _phoneNumber.value = value
        if (value.isNotBlank()) {
            _phoneNumberError.value = null
        }
    }

    fun onEmailChange(value: String) {
        _email.value = value
        if (value.isNotBlank()) {
            _emailError.value = null
        }
    }

    fun onCompanyChange(value: String) {
        _company.value = value
    }

    fun onAddressChange(value: String) {
        _address.value = value
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (_firstName.value.isBlank()) {
            _firstNameError.value = "El nombre es obligatorio"
            isValid = false
        }

        if (_lastName.value.isBlank()) {
            _lastNameError.value = "El apellido es obligatorio"
            isValid = false
        }

        if (_phoneNumber.value.isBlank()) {
            _phoneNumberError.value = "El telefono es obligatorio"
            isValid = false
        } else if (!isValidPhoneNumber(_phoneNumber.value)) {
            _phoneNumberError.value = "Formato de telefono invalido"
            isValid = false
        }

        // El email es opcional, pero si lo completan tiene que ser valido
        if (_email.value.isNotBlank() && !isValidEmail(_email.value)) {
            _emailError.value = "Formato de email invalido"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^[+]?[0-9\\s\\-()]{7,}$".toRegex()
        return phone.matches(phoneRegex)
    }

    fun saveContact(): Boolean {
        if (!validateForm()) {
            return false
        }

        val newContact = Contact(
            firstName = _firstName.value.trim(),
            lastName = _lastName.value.trim(),
            phoneNumber = _phoneNumber.value.trim(),
            email = _email.value.trim(),
            company = _company.value.trim(),
            address = _address.value.trim(),
            isFavorite = false
        )

        repository.addContact(newContact)
        _saveSuccess.value = true

        return true
    }

    fun clearForm() {
        _firstName.value = ""
        _lastName.value = ""
        _phoneNumber.value = ""
        _email.value = ""
        _company.value = ""
        _address.value = ""
        _firstNameError.value = null
        _lastNameError.value = null
        _phoneNumberError.value = null
        _emailError.value = null
        _saveSuccess.value = false
    }
}