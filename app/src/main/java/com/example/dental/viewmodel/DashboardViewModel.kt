package com.example.dental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.Appointment
import com.example.dental.data.model.DashboardNotification
import com.example.dental.data.repository.AppointmentRepository
import com.example.dental.service.EmailReminderService
import com.example.dental.service.NotificationService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = AppointmentRepository()
    private val notificationService = NotificationService(application)
    private val emailService = EmailReminderService()
    
    private val _upcomingAppointments = MutableLiveData<List<Appointment>>()
    val upcomingAppointments: LiveData<List<Appointment>> = _upcomingAppointments
    
    private val _notifications = MutableLiveData<List<DashboardNotification>>()
    val notifications: LiveData<List<DashboardNotification>> = _notifications
    
    private val dashboardNotificationsList = mutableListOf<DashboardNotification>()
    
    fun loadDashboardData() {
        viewModelScope.launch {
            repository.appointments.collect { appointments ->
                val upcoming = repository.getUpcomingAppointments()
                _upcomingAppointments.value = upcoming
                
                // Check for appointments needing reminders
                checkAndSendReminders(upcoming)
                
                // Generate dashboard notifications
                generateDashboardNotifications(upcoming)
            }
        }
    }
    
    private fun checkAndSendReminders(appointments: List<Appointment>) {
        appointments.forEach { appointment ->
            // Check if appointment is within 24 hours
            if (isWithin24Hours(appointment)) {
                // Send notification
                notificationService.sendAppointmentReminder(appointment, 24)
                
                // Send email reminder (simulated)
                emailService.sendAppointmentReminder(
                    "patient@example.com", // Replace with actual user email
                    appointment,
                    24
                )
            }
        }
    }
    
    private fun generateDashboardNotifications(appointments: List<Appointment>) {
        dashboardNotificationsList.clear()
        
        appointments.forEachIndexed { index, appointment ->
            val notification = DashboardNotification(
                id = "NOTIF_${appointment.id}_${index}",
                title = "Upcoming Appointment",
                message = "Appointment with ${appointment.dentistName} on ${appointment.date} at ${appointment.time}",
                timestamp = System.currentTimeMillis() - (index * 3600000), // Stagger timestamps
                isRead = false,
                appointmentId = appointment.id
            )
            dashboardNotificationsList.add(notification)
        }
        
        // Add a welcome notification
        dashboardNotificationsList.add(
            DashboardNotification(
                id = "WELCOME",
                title = "Welcome to Dental Care",
                message = "Manage your appointments, view reminders, and stay on top of your dental health!",
                timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
                isRead = false,
                appointmentId = ""
            )
        )
        
        _notifications.value = dashboardNotificationsList.sortedByDescending { it.timestamp }
    }
    
    private fun isWithin24Hours(appointment: Appointment): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val appointmentDateTime = "${appointment.date} ${appointment.time}"
            val appointmentDate = dateFormat.parse(appointmentDateTime.replace(" AM", "").replace(" PM", ""))
            
            appointmentDate?.let {
                val now = Date()
                val diff = it.time - now.time
                val hoursUntil = diff / (1000 * 60 * 60)
                hoursUntil in 0..24
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    fun markNotificationAsRead(notificationId: String) {
        val updated = dashboardNotificationsList.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        dashboardNotificationsList.clear()
        dashboardNotificationsList.addAll(updated)
        _notifications.value = dashboardNotificationsList.sortedByDescending { it.timestamp }
    }
}
