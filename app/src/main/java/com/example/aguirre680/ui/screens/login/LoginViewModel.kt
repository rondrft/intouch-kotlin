package com.example.aguirre680.ui.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
        if (_errorMessage.value != null) {
            _errorMessage.value = null
        }
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        if (_errorMessage.value != null) {
            _errorMessage.value = null
        }
    }

    fun login() {
        if (_username.value.isBlank()) {
            _errorMessage.value = "El usuario no puede estar vacio"
            return
        }

        if (_password.value.isBlank()) {
            _errorMessage.value = "La contraseña no puede estar vacia"
            return
        }

        if (_password.value.length < 4) {
            _errorMessage.value = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        _isLoading.value = true

        // Credenciales hardcodeadas: admin/1234 o user/pass
        val isValid = when {
            _username.value.equals("admin", ignoreCase = true) &&
                    _password.value == "1234" -> true
            _username.value.equals("user", ignoreCase = true) &&
                    _password.value == "pass" -> true
            else -> false
        }

        Thread.sleep(500) // Simulo un delay como si fuera una API

        _isLoading.value = false

        if (isValid) {
            _isAuthenticated.value = true
            _errorMessage.value = null
        } else {
            _errorMessage.value = "Usuario o contraseña incorrectos"
            _isAuthenticated.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        _isAuthenticated.value = false
        _username.value = ""
        _password.value = ""
        _errorMessage.value = null
    }
}