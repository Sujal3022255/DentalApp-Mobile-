package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Appointment
import com.example.dental.ui.adapter.AppointmentAdapter
import com.example.dental.viewmodel.AppointmentViewModel
import com.google.android.material.tabs.TabLayout

class MyAppointmentsActivity : AppCompatActivity() {
    
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var btnBack: ImageView
    private lateinit var tabLayout: TabLayout
    private lateinit var appointmentsRecyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var newAppointmentButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)
        
        // Hide default action bar
        supportActionBar?.hide()
        
        initViews()
        setupTabs()
        observeViewModel()
    }
    
    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tabLayout = findViewById(R.id.appointments_tab_layout)
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view)
        newAppointmentButton = findViewById(R.id.new_appointment_button)
        
        btnBack.setOnClickListener {
            finish()
        }
        
        appointmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        appointmentAdapter = AppointmentAdapter(
            onCancelClick = { appointment -> cancelAppointment(appointment) },
            onRescheduleClick = { appointment -> rescheduleAppointment(appointment) }
        )
        appointmentsRecyclerView.adapter = appointmentAdapter
        
        newAppointmentButton.setOnClickListener {
            val intent = Intent(this, BrowseDentistsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("UPCOMING"))
        tabLayout.addTab(tabLayout.newTab().setText("PAST"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadUpcomingAppointments()
                    1 -> loadPastAppointments()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        // Load upcoming by default
        loadUpcomingAppointments()
    }
    
    private fun observeViewModel() {
        viewModel.upcomingAppointments.observe(this) { appointments ->
            if (tabLayout.selectedTabPosition == 0) {
                appointmentAdapter.submitList(appointments)
            }
        }
        
        viewModel.pastAppointments.observe(this) { appointments ->
            if (tabLayout.selectedTabPosition == 1) {
                appointmentAdapter.submitList(appointments)
            }
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
    
    private fun loadUpcomingAppointments() {
        viewModel.upcomingAppointments.value?.let {
            appointmentAdapter.submitList(it)
        }
    }
    
    private fun loadPastAppointments() {
        viewModel.pastAppointments.value?.let {
            appointmentAdapter.submitList(it)
        }
    }
    
    private fun cancelAppointment(appointment: Appointment) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to cancel this appointment?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.cancelAppointment(appointment.id)
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun rescheduleAppointment(appointment: Appointment) {
        val intent = Intent(this, RescheduleAppointmentActivity::class.java)
        intent.putExtra("APPOINTMENT", appointment)
        startActivity(intent)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
