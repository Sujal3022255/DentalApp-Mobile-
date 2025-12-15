package com.example.dental.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.model.AppointmentStatus
import com.example.dental.model.DentistAppointment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class DentistAppointmentDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var appointment: DentistAppointment
    
    private lateinit var tvPatientName: TextView
    private lateinit var tvPatientEmail: TextView
    private lateinit var tvPatientPhone: TextView
    private lateinit var tvAppointmentDate: TextView
    private lateinit var tvAppointmentTime: TextView
    private lateinit var tvTreatmentType: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvPatientNotes: TextView
    private lateinit var etTreatmentNotes: TextInputEditText
    private lateinit var etPrescription: TextInputEditText
    private lateinit var toggleGroupStatus: MaterialButtonToggleGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dentist_appointment_detail)

        db = FirebaseFirestore.getInstance()
        
        // Safely get appointment with null check
        appointment = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("appointment", DentistAppointment::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("appointment") as? DentistAppointment
        } ?: run {
            Toast.makeText(this, "Error loading appointment data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializeViews()
        loadAppointmentData()
        setupListeners()
    }

    private fun initializeViews() {
        tvPatientName = findViewById(R.id.tvPatientName)
        tvPatientEmail = findViewById(R.id.tvPatientEmail)
        tvPatientPhone = findViewById(R.id.tvPatientPhone)
        tvAppointmentDate = findViewById(R.id.tvAppointmentDate)
        tvAppointmentTime = findViewById(R.id.tvAppointmentTime)
        tvTreatmentType = findViewById(R.id.tvTreatmentType)
        tvStatus = findViewById(R.id.tvStatus)
        tvPatientNotes = findViewById(R.id.tvPatientNotes)
        etTreatmentNotes = findViewById(R.id.etTreatmentNotes)
        etPrescription = findViewById(R.id.etPrescription)
        toggleGroupStatus = findViewById(R.id.toggleGroupStatus)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun loadAppointmentData() {
        tvPatientName.text = appointment.patientName
        tvPatientEmail.text = appointment.patientEmail
        tvPatientPhone.text = appointment.patientPhone
        tvAppointmentDate.text = appointment.date
        tvAppointmentTime.text = appointment.time
        tvTreatmentType.text = appointment.treatmentType
        tvStatus.text = appointment.status.displayName
        
        // Set status background
        val statusBackground = when (appointment.status) {
            AppointmentStatus.PENDING -> R.drawable.bg_status_pending
            AppointmentStatus.CONFIRMED -> R.drawable.bg_status_confirmed
            AppointmentStatus.COMPLETED -> R.drawable.bg_status_completed
            AppointmentStatus.CANCELLED -> R.drawable.bg_status_cancelled
            AppointmentStatus.NO_SHOW -> R.drawable.bg_status_cancelled
        }
        tvStatus.setBackgroundResource(statusBackground)
        
        // Load existing notes
        if (appointment.notes.isNotEmpty()) {
            tvPatientNotes.text = appointment.notes
        }
        
        if (appointment.treatmentNotes.isNotEmpty()) {
            etTreatmentNotes.setText(appointment.treatmentNotes)
        }
        
        if (appointment.prescription.isNotEmpty()) {
            etPrescription.setText(appointment.prescription)
        }
        
        // Set current status in toggle group
        when (appointment.status) {
            AppointmentStatus.PENDING -> toggleGroupStatus.check(R.id.btnStatusPending)
            AppointmentStatus.CONFIRMED -> toggleGroupStatus.check(R.id.btnStatusConfirmed)
            AppointmentStatus.COMPLETED -> toggleGroupStatus.check(R.id.btnStatusCompleted)
            AppointmentStatus.CANCELLED -> toggleGroupStatus.check(R.id.btnStatusCancelled)
            else -> toggleGroupStatus.check(R.id.btnStatusPending)
        }
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btnSaveStatus).setOnClickListener {
            updateAppointmentStatus()
        }

        findViewById<Button>(R.id.btnAddTreatmentNote).setOnClickListener {
            saveTreatmentNotes()
        }

        findViewById<Button>(R.id.btnAddPrescription).setOnClickListener {
            savePrescription()
        }

        findViewById<Button>(R.id.btnCallPatient).setOnClickListener {
            callPatient()
        }

        findViewById<Button>(R.id.btnViewHistory).setOnClickListener {
            viewPatientHistory()
        }
    }

    private fun updateAppointmentStatus() {
        val selectedStatus = when (toggleGroupStatus.checkedButtonId) {
            R.id.btnStatusPending -> AppointmentStatus.PENDING
            R.id.btnStatusConfirmed -> AppointmentStatus.CONFIRMED
            R.id.btnStatusCompleted -> AppointmentStatus.COMPLETED
            R.id.btnStatusCancelled -> AppointmentStatus.CANCELLED
            else -> AppointmentStatus.PENDING
        }

        db.collection("appointments").document(appointment.id)
            .update(
                mapOf(
                    "status" to selectedStatus.name,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
                appointment = appointment.copy(status = selectedStatus)
                loadAppointmentData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update status: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveTreatmentNotes() {
        val notes = etTreatmentNotes.text.toString()
        if (notes.isEmpty()) {
            Toast.makeText(this, "Please enter treatment notes", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("appointments").document(appointment.id)
            .update(
                mapOf(
                    "treatmentNotes" to notes,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Treatment notes saved", Toast.LENGTH_SHORT).show()
                
                // Also save to treatment history
                saveTreatmentHistory(notes)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveTreatmentHistory(notes: String) {
        val treatmentHistory = hashMapOf(
            "appointmentId" to appointment.id,
            "patientId" to appointment.patientId,
            "dentistId" to appointment.dentistId,
            "date" to appointment.date,
            "treatmentType" to appointment.treatmentType,
            "notes" to notes,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("treatmentHistory")
            .add(treatmentHistory)
            .addOnSuccessListener {
                // Success
            }
    }

    private fun savePrescription() {
        val prescription = etPrescription.text.toString()
        if (prescription.isEmpty()) {
            Toast.makeText(this, "Please enter prescription", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("appointments").document(appointment.id)
            .update(
                mapOf(
                    "prescription" to prescription,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Prescription saved", Toast.LENGTH_SHORT).show()
                
                // Also save to prescriptions collection
                savePrescriptionHistory(prescription)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save prescription: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePrescriptionHistory(prescription: String) {
        val prescriptionData = hashMapOf(
            "appointmentId" to appointment.id,
            "patientId" to appointment.patientId,
            "dentistId" to appointment.dentistId,
            "date" to appointment.date,
            "prescription" to prescription,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("prescriptions")
            .add(prescriptionData)
            .addOnSuccessListener {
                // Success
            }
    }

    private fun callPatient() {
        val phoneNumber = appointment.patientPhone
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun viewPatientHistory() {
        // TODO: Implement patient history view
        Toast.makeText(this, "Patient history feature coming soon", Toast.LENGTH_SHORT).show()
    }
}
