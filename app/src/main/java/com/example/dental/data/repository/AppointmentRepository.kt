package com.example.dental.data.repository

import com.example.dental.data.model.Appointment
import com.example.dental.data.model.AppointmentStatus
import com.example.dental.data.model.Dentist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppointmentRepository {
    
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments
    
    private val _dentists = MutableStateFlow<List<Dentist>>(getSampleDentists())
    val dentists: StateFlow<List<Dentist>> = _dentists
    
    fun bookAppointment(appointment: Appointment): Result<String> {
        return try {
            val newAppointment = appointment.copy(id = generateId())
            _appointments.value = _appointments.value + newAppointment
            Result.success("Appointment booked successfully!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun cancelAppointment(appointmentId: String): Result<String> {
        return try {
            _appointments.value = _appointments.value.map {
                if (it.id == appointmentId) {
                    it.copy(status = AppointmentStatus.CANCELLED)
                } else it
            }
            Result.success("Appointment cancelled successfully!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String): Result<String> {
        return try {
            _appointments.value = _appointments.value.map {
                if (it.id == appointmentId) {
                    it.copy(
                        date = newDate,
                        time = newTime,
                        status = AppointmentStatus.RESCHEDULED
                    )
                } else it
            }
            Result.success("Appointment rescheduled successfully!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getUpcomingAppointments(): List<Appointment> {
        return _appointments.value.filter { 
            it.status == AppointmentStatus.SCHEDULED || it.status == AppointmentStatus.RESCHEDULED
        }
    }
    
    fun getPastAppointments(): List<Appointment> {
        return _appointments.value.filter { 
            it.status == AppointmentStatus.COMPLETED || it.status == AppointmentStatus.CANCELLED
        }
    }
    
    private fun generateId(): String {
        return "APT${System.currentTimeMillis()}"
    }
    
    private fun getSampleDentists(): List<Dentist> {
        return listOf(
            Dentist(
                id = "D001",
                name = "Dr. Sarah Johnson",
                specialty = "General Dentistry",
                experience = 10,
                rating = 4.8f,
                availableDays = listOf("Monday", "Tuesday", "Wednesday", "Friday"),
                availableTimeSlots = listOf("09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM", "04:00 PM")
            ),
            Dentist(
                id = "D002",
                name = "Dr. Michael Chen",
                specialty = "Orthodontics",
                experience = 15,
                rating = 4.9f,
                availableDays = listOf("Monday", "Wednesday", "Thursday", "Friday"),
                availableTimeSlots = listOf("09:00 AM", "10:30 AM", "01:00 PM", "02:30 PM", "04:00 PM")
            ),
            Dentist(
                id = "D003",
                name = "Dr. Emily Brown",
                specialty = "Cosmetic Dentistry",
                experience = 8,
                rating = 4.7f,
                availableDays = listOf("Tuesday", "Wednesday", "Thursday", "Saturday"),
                availableTimeSlots = listOf("10:00 AM", "11:30 AM", "01:30 PM", "03:00 PM", "04:30 PM")
            ),
            Dentist(
                id = "D004",
                name = "Dr. James Wilson",
                specialty = "Endodontics",
                experience = 12,
                rating = 4.9f,
                availableDays = listOf("Monday", "Tuesday", "Thursday", "Friday"),
                availableTimeSlots = listOf("09:30 AM", "11:00 AM", "02:00 PM", "03:30 PM", "05:00 PM")
            )
        )
    }
}
