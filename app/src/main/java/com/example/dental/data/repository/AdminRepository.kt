package com.example.dental.data.repository

import android.content.Context
import com.example.dental.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

class AdminRepository(private val context: Context) {
    
    private val firestore = FirebaseFirestore.getInstance()
    
    // ============ Dashboard Statistics ============
    
    suspend fun getAppointmentStats(): Result<AppointmentStats> {
        return try {
            val appointments = firestore.collection("appointments")
                .get()
                .await()
            
            val now = Calendar.getInstance()
            val todayStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis
            
            val weekStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }.timeInMillis
            
            val monthStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }.timeInMillis
            
            var todayCount = 0
            var weeklyCount = 0
            var monthlyCount = 0
            var pendingCount = 0
            var approvedCount = 0
            var completedCount = 0
            var cancelledCount = 0
            
            appointments.documents.forEach { doc ->
                val timestamp = doc.getLong("timestamp") ?: 0
                val status = doc.getString("status") ?: ""
                
                if (timestamp >= todayStart) todayCount++
                if (timestamp >= weekStart) weeklyCount++
                if (timestamp >= monthStart) monthlyCount++
                
                when (status.lowercase()) {
                    "pending" -> pendingCount++
                    "approved", "confirmed" -> approvedCount++
                    "completed" -> completedCount++
                    "cancelled" -> cancelledCount++
                }
            }
            
            Result.success(AppointmentStats(
                totalAppointments = appointments.size(),
                todayAppointments = todayCount,
                weeklyAppointments = weeklyCount,
                monthlyAppointments = monthlyCount,
                pendingAppointments = pendingCount,
                approvedAppointments = approvedCount,
                completedAppointments = completedCount,
                cancelledAppointments = cancelledCount
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserStats(): Result<UserStats> {
        return try {
            val users = firestore.collection("users").get().await()
            
            val weekStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, 0)
            }.timeInMillis
            
            val monthStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
            }.timeInMillis
            
            var patientCount = 0
            var dentistCount = 0
            var adminCount = 0
            var newWeekCount = 0
            var newMonthCount = 0
            
            users.documents.forEach { doc ->
                val role = doc.getString("role") ?: "patient"
                val createdAt = doc.getLong("createdAt") ?: 0
                
                when (role.lowercase()) {
                    "patient" -> patientCount++
                    "dentist" -> dentistCount++
                    "admin" -> adminCount++
                }
                
                if (createdAt >= weekStart) newWeekCount++
                if (createdAt >= monthStart) newMonthCount++
            }
            
            Result.success(UserStats(
                totalPatients = patientCount,
                totalDentists = dentistCount,
                totalAdmins = adminCount,
                activeUsers = users.size(),
                newPatientsThisWeek = newWeekCount,
                newPatientsThisMonth = newMonthCount
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============ User Management ============
    
    suspend fun getAllDentists(): Result<List<Dentist>> {
        return try {
            val dentists = firestore.collection("dentists")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Dentist::class.java) }
            Result.success(dentists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addDentist(dentist: Dentist): Result<String> {
        return try {
            val docRef = firestore.collection("dentists").document()
            val newDentist = dentist.copy(id = docRef.id)
            docRef.set(newDentist).await()
            Result.success("Dentist added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateDentist(dentist: Dentist): Result<String> {
        return try {
            firestore.collection("dentists")
                .document(dentist.id)
                .set(dentist)
                .await()
            Result.success("Dentist updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteDentist(dentistId: String): Result<String> {
        return try {
            firestore.collection("dentists")
                .document(dentistId)
                .delete()
                .await()
            Result.success("Dentist removed successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllPatients(): Result<List<User>> {
        return try {
            val patients = firestore.collection("users")
                .whereEqualTo("role", "patient")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(User::class.java) }
            Result.success(patients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserRole(userId: String, role: String): Result<String> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("role", role)
                .await()
            Result.success("User role updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============ Appointment Management ============
    
    suspend fun getAllAppointments(): Result<List<Appointment>> {
        return try {
            val appointments = firestore.collection("appointments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Appointment::class.java) }
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun approveAppointment(appointmentId: String): Result<String> {
        return try {
            firestore.collection("appointments")
                .document(appointmentId)
                .update("status", "approved")
                .await()
            Result.success("Appointment approved")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun declineAppointment(appointmentId: String, reason: String): Result<String> {
        return try {
            firestore.collection("appointments")
                .document(appointmentId)
                .update(mapOf(
                    "status" to "cancelled",
                    "cancellationReason" to reason
                ))
                .await()
            Result.success("Appointment declined")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTimeSlots(dentistId: String): Result<List<TimeSlot>> {
        return try {
            val slots = firestore.collection("timeSlots")
                .whereEqualTo("dentistId", dentistId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(TimeSlot::class.java) }
            Result.success(slots)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addTimeSlot(slot: TimeSlot): Result<String> {
        return try {
            val docRef = firestore.collection("timeSlots").document()
            val newSlot = slot.copy(id = docRef.id)
            docRef.set(newSlot).await()
            Result.success("Time slot added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteTimeSlot(slotId: String): Result<String> {
        return try {
            firestore.collection("timeSlots")
                .document(slotId)
                .delete()
                .await()
            Result.success("Time slot deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============ Content Management ============
    
    suspend fun getAllDentalTips(): Result<List<DentalTip>> {
        return try {
            val tips = firestore.collection("dentalTips")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(DentalTip::class.java) }
            Result.success(tips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addDentalTip(tip: DentalTip): Result<String> {
        return try {
            val docRef = firestore.collection("dentalTips").document()
            val newTip = tip.copy(id = docRef.id)
            docRef.set(newTip).await()
            Result.success("Dental tip added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateDentalTip(tip: DentalTip): Result<String> {
        return try {
            firestore.collection("dentalTips")
                .document(tip.id)
                .set(tip)
                .await()
            Result.success("Dental tip updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteDentalTip(tipId: String): Result<String> {
        return try {
            firestore.collection("dentalTips")
                .document(tipId)
                .delete()
                .await()
            Result.success("Dental tip deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
