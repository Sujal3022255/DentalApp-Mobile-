package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.MainActivity
import com.example.dental.R
import com.example.dental.data.model.Appointment
import com.example.dental.ui.adapter.DashboardAppointmentAdapter
import com.example.dental.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {
    
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var upcomingAppointmentsRecyclerView: RecyclerView
    private lateinit var noAppointmentsText: TextView
    private lateinit var viewAllAppointments: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        // Hide action bar for custom header
        supportActionBar?.hide()
        
        initViews()
        setupMenuItems()
        setupAppointments()
        observeViewModel()
    }
    
    private fun initViews() {
        upcomingAppointmentsRecyclerView = findViewById(R.id.upcoming_appointments_recycler)
        noAppointmentsText = findViewById(R.id.no_appointments_text)
        viewAllAppointments = findViewById(R.id.view_all_appointments)
        
        // Back button handler
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupMenuItems() {
        // Appointments
        findViewById<LinearLayout>(R.id.menu_appointments).setOnClickListener {
            startActivity(Intent(this, MyAppointmentsActivity::class.java))
        }
        
        // Browse Dentists
        findViewById<LinearLayout>(R.id.menu_browse).setOnClickListener {
            startActivity(Intent(this, BrowseDentistsActivity::class.java))
        }
        
        // Profile
        findViewById<LinearLayout>(R.id.menu_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // Dental History
        findViewById<LinearLayout>(R.id.menu_history).setOnClickListener {
            startActivity(Intent(this, DentalHistoryActivity::class.java))
        }
        
        // Emergency
        findViewById<LinearLayout>(R.id.menu_emergency).setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
        }
        
        // Reminders (Dashboard)
        findViewById<LinearLayout>(R.id.menu_reminders).setOnClickListener {
            // Stay on dashboard or show reminders
        }
        
        // Treatment Plans
        findViewById<LinearLayout>(R.id.menu_treatment).setOnClickListener {
            startActivity(Intent(this, DentalHistoryActivity::class.java))
        }
        
        // Insurance
        findViewById<LinearLayout>(R.id.menu_insurance).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // Settings
        findViewById<LinearLayout>(R.id.menu_settings).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // View All Appointments
        viewAllAppointments.setOnClickListener {
            startActivity(Intent(this, MyAppointmentsActivity::class.java))
        }
    }
    
    private fun setupAppointments() {
        upcomingAppointmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        val appointmentAdapter = DashboardAppointmentAdapter { appointment ->
            openAppointmentDetails(appointment)
        }
        upcomingAppointmentsRecyclerView.adapter = appointmentAdapter
        
        viewModel.upcomingAppointments.observe(this) { appointments ->
            val upcomingList = appointments.take(2) // Show only 2 upcoming
            appointmentAdapter.submitList(upcomingList)
            
            // Show/hide empty state
            if (upcomingList.isEmpty()) {
                upcomingAppointmentsRecyclerView.visibility = View.GONE
                noAppointmentsText.visibility = View.VISIBLE
            } else {
                upcomingAppointmentsRecyclerView.visibility = View.VISIBLE
                noAppointmentsText.visibility = View.GONE
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.loadDashboardData()
    }
    
    private fun openAppointmentDetails(appointment: Appointment) {
        val intent = Intent(this, MyAppointmentsActivity::class.java)
        startActivity(intent)
    }
    
    override fun onBackPressed() {
        // Navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
