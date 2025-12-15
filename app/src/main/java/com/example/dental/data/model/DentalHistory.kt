package com.example.dental.data.model

import java.io.Serializable

data class Treatment(
    val id: String = "",
    val patientId: String = "",
    val dentistName: String = "",
    val treatmentType: String = "",
    val description: String = "",
    val date: String = "",
    val cost: Double = 0.0,
    val status: TreatmentStatus = TreatmentStatus.COMPLETED,
    val notes: String = ""
) : Serializable

enum class TreatmentStatus {
    COMPLETED,
    ONGOING,
    SCHEDULED,
    CANCELLED
}

data class Prescription(
    val id: String = "",
    val patientId: String = "",
    val dentistName: String = "",
    val medicationName: String = "",
    val dosage: String = "",
    val frequency: String = "",
    val duration: String = "",
    val date: String = "",
    val instructions: String = "",
    val refillsAllowed: Int = 0
) : Serializable

data class MedicalReport(
    val id: String = "",
    val patientId: String = "",
    val reportType: String = "",
    val title: String = "",
    val date: String = "",
    val dentistName: String = "",
    val findings: String = "",
    val recommendations: String = "",
    val fileUrl: String = ""
) : Serializable
