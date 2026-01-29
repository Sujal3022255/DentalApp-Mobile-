package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.LoginActivity
import com.example.dental.viewmodel.AdminViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {
    
    private val viewModel: AdminViewModel by viewModels()
    
    private lateinit var tvTodayCount: TextView
    private lateinit var tvTotalAppointments: TextView
    private lateinit var tvPendingCount: TextView
    private lateinit var tvCompletedCount: TextView
    private lateinit var tvPatientCount: TextView
    private lateinit var tvDentistCount: TextView
    private lateinit var tvAdminCount: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Security check - verify admin role
        checkAdminRole()
        
        setContentView(R.layout.activity_admin_dashboard)
        
        supportActionBar?.hide()
        
        initViews()
        observeViewModel()
        setupClickListeners()
        
        // Load dashboard data
        viewModel.loadDashboardStats()
    }
    
    private fun initViews() {
        tvTodayCount = findViewById(R.id.tvTodayCount)
        tvTotalAppointments = findViewById(R.id.tvTotalAppointments)
        tvPendingCount = findViewById(R.id.tvPendingCount)
        tvCompletedCount = findViewById(R.id.tvCompletedCount)
        tvPatientCount = findViewById(R.id.tvPatientCount)
        tvDentistCount = findViewById(R.id.tvDentistCount)
        tvAdminCount = findViewById(R.id.tvAdminCount)
        
        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
    
    private fun observeViewModel() {
        viewModel.appointmentStats.observe(this) { stats ->
            tvTodayCount.text = stats.todayAppointments.toString()
            tvTotalAppointments.text = stats.totalAppointments.toString()
            tvPendingCount.text = stats.pendingAppointments.toString()
            tvCompletedCount.text = stats.completedAppointments.toString()
        }
        
        viewModel.userStats.observe(this) { stats ->
            tvPatientCount.text = stats.totalPatients.toString()
            tvDentistCount.text = stats.totalDentists.toString()
            tvAdminCount.text = stats.totalAdmins.toString()
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
    
    private fun setupClickListeners() {
        findViewById<Button>(R.id.btnManageUsers).setOnClickListener {
            startActivity(Intent(this, ManageUsersActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnManageAppointments).setOnClickListener {
            startActivity(Intent(this, ManageAppointmentsActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnManageContent).setOnClickListener {
            startActivity(Intent(this, ManageContentActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnAnalytics).setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadDashboardStats()
    }
    
    private fun checkAdminRole() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            redirectToLogin("Please login first")
            return
        }
        
        FirebaseFirestore.getInstance().collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")?.lowercase() ?: "patient"
                if (role != "admin") {
                    redirectToLogin("Access Denied: Admin privileges required")
                }
            }
            .addOnFailureListener {
                redirectToLogin("Access Denied: Unable to verify role")
            }
    }
    
    private fun redirectToLogin(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
