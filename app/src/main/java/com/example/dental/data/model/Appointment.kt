package com.example.dental.data.model

import java.io.Serializable

data class Appointment(
    val id: String = "",
    val userId: String = "",
    val dentistId: String = "",
    val dentistName: String = "",
    val dentistSpecialty: String = "",
    val date: String = "",
    val time: String = "",
    val type: AppointmentType = AppointmentType.CHECKUP,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

enum class AppointmentType {
    CHECKUP,
    CLEANING,
    FILLING,
    ROOT_CANAL,
    EXTRACTION,
    CONSULTATION,
    EMERGENCY
}

enum class AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
    RESCHEDULED
}
