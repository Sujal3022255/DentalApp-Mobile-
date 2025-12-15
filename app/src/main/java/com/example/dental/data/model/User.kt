package com.example.dental.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val profilePictureUri: String = "",
    val emergencyContact: EmergencyContact? = null,
    val createdAt: Long = System.currentTimeMillis()
)
