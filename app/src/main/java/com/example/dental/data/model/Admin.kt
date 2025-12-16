package com.example.dental.data.model

data class Admin(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "admin",
    val createdAt: Long = System.currentTimeMillis()
)

data class DentalTip(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class AppointmentStats(
    val totalAppointments: Int = 0,
    val todayAppointments: Int = 0,
    val weeklyAppointments: Int = 0,
    val monthlyAppointments: Int = 0,
    val pendingAppointments: Int = 0,
    val approvedAppointments: Int = 0,
    val completedAppointments: Int = 0,
    val cancelledAppointments: Int = 0
)

data class UserStats(
    val totalPatients: Int = 0,
    val totalDentists: Int = 0,
    val totalAdmins: Int = 0,
    val activeUsers: Int = 0,
    val newPatientsThisWeek: Int = 0,
    val newPatientsThisMonth: Int = 0
)

data class TimeSlot(
    val id: String = "",
    val dentistId: String = "",
    val dentistName: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isAvailable: Boolean = true,
    val isBooked: Boolean = false,
    val appointmentId: String = ""
)
