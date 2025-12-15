package com.example.dental.service

import android.util.Log
import com.example.dental.data.model.Appointment
import java.text.SimpleDateFormat
import java.util.*

class EmailReminderService {
    
    fun sendAppointmentReminder(
        email: String,
        appointment: Appointment,
        hoursBeforeAppointment: Int
    ): Boolean {
        return try {
            // Simulate sending email
            val emailContent = buildEmailContent(appointment, hoursBeforeAppointment)
            
            Log.d("EmailReminderService", "Sending reminder to: $email")
            Log.d("EmailReminderService", "Subject: Upcoming Appointment Reminder")
            Log.d("EmailReminderService", "Content: $emailContent")
            
            // In a real app, you would integrate with an email service like SendGrid, AWS SES, etc.
            // For now, we'll simulate a successful send
            simulateEmailSend(email, "Upcoming Appointment Reminder", emailContent)
            
            true
        } catch (e: Exception) {
            Log.e("EmailReminderService", "Error sending email: ${e.message}")
            false
        }
    }
    
    private fun buildEmailContent(appointment: Appointment, hoursBeforeAppointment: Int): String {
        return """
            Dear Patient,
            
            This is a friendly reminder about your upcoming dental appointment.
            
            Appointment Details:
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Doctor: ${appointment.dentistName}
            Specialty: ${appointment.dentistSpecialty}
            Date: ${appointment.date}
            Time: ${appointment.time}
            Type: ${appointment.type.name.replace("_", " ")}
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            Your appointment is in $hoursBeforeAppointment hours.
            
            Please arrive 10 minutes early for check-in.
            
            If you need to reschedule or cancel, please contact us as soon as possible.
            
            ${if (appointment.notes.isNotEmpty()) "\nAdditional Notes: ${appointment.notes}\n" else ""}
            
            We look forward to seeing you!
            
            Best regards,
            Dental Care Management Team
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            This is an automated reminder. Please do not reply to this email.
        """.trimIndent()
    }
    
    private fun simulateEmailSend(to: String, subject: String, body: String) {
        // Simulate email sending delay
        Thread.sleep(100)
        
        // Log the simulated email
        println("═══════════════════════════════════════════════")
        println("📧 EMAIL SENT")
        println("To: $to")
        println("Subject: $subject")
        println("───────────────────────────────────────────────")
        println(body)
        println("═══════════════════════════════════════════════")
    }
    
    fun sendConfirmationEmail(email: String, appointment: Appointment): Boolean {
        return try {
            val emailContent = """
                Dear Patient,
                
                Your appointment has been successfully scheduled!
                
                Appointment Details:
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Doctor: ${appointment.dentistName}
                Specialty: ${appointment.dentistSpecialty}
                Date: ${appointment.date}
                Time: ${appointment.time}
                Type: ${appointment.type.name.replace("_", " ")}
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                
                You will receive a reminder email 24 hours before your appointment.
                
                Thank you for choosing our dental care services!
                
                Best regards,
                Dental Care Management Team
            """.trimIndent()
            
            simulateEmailSend(email, "Appointment Confirmation", emailContent)
            true
        } catch (e: Exception) {
            Log.e("EmailReminderService", "Error sending confirmation: ${e.message}")
            false
        }
    }
}
