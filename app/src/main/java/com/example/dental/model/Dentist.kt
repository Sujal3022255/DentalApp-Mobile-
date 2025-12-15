package com.example.dental.model

import java.io.Serializable

data class Dentist(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val specialization: String = "",
    val experience: String = "",
    val education: String = "",
    val licenseNumber: String = "",
    val imageUrl: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val availableDays: List<String> = emptyList(),
    val consultationFee: String = "",
    val languages: String = "",
    val address: String = "",
    val isActive: Boolean = true,
    val role: String = "dentist", // "dentist" or "patient"
    val createdAt: Long = System.currentTimeMillis()
) : Serializable
