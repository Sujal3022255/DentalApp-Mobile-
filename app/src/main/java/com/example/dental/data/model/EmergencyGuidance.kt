package com.example.dental.data.model

import java.io.Serializable

data class EmergencyGuidance(
    val id: String = "",
    val title: String = "",
    val severity: EmergencySeverity = EmergencySeverity.MODERATE,
    val symptoms: List<String> = emptyList(),
    val immediateSteps: List<String> = emptyList(),
    val dosList: List<String> = emptyList(),
    val dontsList: List<String> = emptyList(),
    val whenToCallDentist: String = "",
    val icon: String = ""
) : Serializable

enum class EmergencySeverity {
    CRITICAL,
    HIGH,
    MODERATE,
    LOW
}

data class EmergencyDentalContact(
    val name: String = "",
    val phone: String = "",
    val type: ContactType = ContactType.DENTIST,
    val available247: Boolean = false
)

enum class ContactType {
    DENTIST,
    EMERGENCY_DENTAL,
    HOSPITAL,
    POISON_CONTROL
}
