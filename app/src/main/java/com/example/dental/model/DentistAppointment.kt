package com.example.dental.model

import java.io.Serializable

data class DentistAppointment(
    val id: String = "",
    val patientId: String = "",
    val patientName: String = "",
    val patientEmail: String = "",
    val patientPhone: String = "",
    val dentistId: String = "",
    val dentistName: String = "",
    val date: String = "",
    val time: String = "",
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val treatmentType: String = "",
    val notes: String = "",
    val treatmentNotes: String = "",
    val prescription: String = "",
    val dentalHistory: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable

enum class AppointmentStatus(val displayName: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    NO_SHOW("No Show")
}

data class TreatmentNote(
    val id: String = "",
    val appointmentId: String = "",
    val patientId: String = "",
    val dentistId: String = "",
    val date: String = "",
    val diagnosis: String = "",
    val treatment: String = "",
    val prescription: String = "",
    val nextVisit: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

data class DentistSchedule(
    val id: String = "",
    val dentistId: String = "",
    val dayOfWeek: String = "", // Monday, Tuesday, etc.
    val startTime: String = "",
    val endTime: String = "",
    val slotDuration: Int = 30, // in minutes
    val isAvailable: Boolean = true,
    val blockedDates: List<String> = emptyList() // List of blocked dates (yyyy-MM-dd)
) : Serializable

data class TimeSlot(
    val time: String = "",
    val isAvailable: Boolean = true,
    val isBooked: Boolean = false,
    val appointmentId: String? = null
) : Serializable
