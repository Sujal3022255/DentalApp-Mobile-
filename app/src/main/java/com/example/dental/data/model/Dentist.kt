package com.example.dental.data.model

import java.io.Serializable

data class Dentist(
    val id: String = "",
    val name: String = "",
    val specialty: String = "",
    val experience: Int = 0,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val availableDays: List<String> = listOf(),
    val availableTimeSlots: List<String> = listOf()
) : Serializable
