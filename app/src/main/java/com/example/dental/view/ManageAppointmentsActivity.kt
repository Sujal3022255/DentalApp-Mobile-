package com.example.dental.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Appointment
import com.example.dental.viewmodel.AdminViewModel
import com.google.android.material.textfield.TextInputEditText
import com.example.dental.ui.adapter.AdminAppointmentAdapter

class ManageAppointmentsActivity : AppCompatActivity() {
    
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var appointmentAdapter: AdminAppointmentAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_appointments)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Manage Appointments"
        
        initViews()
        observeViewModel()
        
        viewModel.loadAllAppointments()
    }
    
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewAppointments)
        progressBar = findViewById(R.id.progressBar)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        appointmentAdapter = AdminAppointmentAdapter(
            onApproveClick = { appointment -> approveAppointment(appointment.id) },
            onDeclineClick = { appointment -> declineAppointment(appointment.id) }
        )
        recyclerView.adapter = appointmentAdapter
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.appointments.observe(this) { appointments ->
            appointmentAdapter.submitList(appointments)
            Toast.makeText(this, "Loaded ${appointments.size} appointments", Toast.LENGTH_SHORT).show()
        }
        
        viewModel.operationStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    fun approveAppointment(appointmentId: String) {
        AlertDialog.Builder(this)
            .setTitle("Approve Appointment")
            .setMessage("Are you sure you want to approve this appointment?")
            .setPositiveButton("Approve") { _, _ ->
                viewModel.approveAppointment(appointmentId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    fun declineAppointment(appointmentId: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_decline_reason, null)
        val etReason = dialogView.findViewById<TextInputEditText>(R.id.etDeclineReason)
        
        AlertDialog.Builder(this)
            .setTitle("Decline Appointment")
            .setView(dialogView)
            .setPositiveButton("Decline") { _, _ ->
                val reason = etReason.text.toString()
                viewModel.declineAppointment(appointmentId, reason)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
