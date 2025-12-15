package com.example.dental.data.model

data class AppointmentReminder(
    val appointmentId: String = "",
    val reminderTime: Long = 0L,
    val reminderType: ReminderType = ReminderType.EMAIL,
    val sent: Boolean = false,
    val message: String = ""
)

enum class ReminderType {
    EMAIL,
    NOTIFICATION,
    BOTH
}

data class DashboardNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val appointmentId: String = ""
)
