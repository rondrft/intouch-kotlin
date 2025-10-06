package com.example.aguirre680.data.model

data class Contact(
    val id: String = java.util.UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String = "",
    val company: String = "",
    val address: String = "",
    val avatarUrl: String = "",
    val isFavorite: Boolean = false
) {
    fun getFullName(): String = "$firstName $lastName"

    fun getInitials(): String {
        val firstInitial = firstName.firstOrNull()?.uppercaseChar() ?: ""
        val lastInitial = lastName.firstOrNull()?.uppercaseChar() ?: ""
        return "$firstInitial$lastInitial"
    }
}