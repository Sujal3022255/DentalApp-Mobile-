package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.adapter.DentistAppointmentAdapter
import com.example.dental.model.AppointmentStatus
import com.example.dental.model.DentistAppointment
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DentistDashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rvAppointments: RecyclerView
    private lateinit var appointmentAdapter: DentistAppointmentAdapter
    private lateinit var tvDentistName: TextView
    private lateinit var tvTotalAppointments: TextView
    private lateinit var tvConfirmedAppointments: TextView
    private lateinit var tvPendingAppointments: TextView
    private lateinit var emptyStateLayout: View
    
    private var allAppointments = mutableListOf<DentistAppointment>()
    private var currentFilter: AppointmentStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dentist_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        initializeViews()
        setupRecyclerView()
        setupListeners()
        loadDentistProfile()
        loadAppointments()
    }

    private fun initializeViews() {
        tvDentistName = findViewById(R.id.tvDentistName)
        tvTotalAppointments = findViewById(R.id.tvTotalAppointments)
        tvConfirmedAppointments = findViewById(R.id.tvConfirmedAppointments)
        tvPendingAppointments = findViewById(R.id.tvPendingAppointments)
        rvAppointments = findViewById(R.id.rvAppointments)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, com.example.dental.LoginActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btnNotifications).setOnClickListener {
            // TODO: Implement notifications
        }

        findViewById<FloatingActionButton>(R.id.fabAddAppointment).setOnClickListener {
            // TODO: Add new appointment
        }
    }

    private fun setupRecyclerView() {
        appointmentAdapter = DentistAppointmentAdapter(mutableListOf()) { appointment ->
            openAppointmentDetails(appointment)
        }
        rvAppointments.apply {
            layoutManager = LinearLayoutManager(this@DentistDashboardActivity)
            adapter = appointmentAdapter
        }
    }

    private fun setupListeners() {
        // Filter chips
        findViewById<Chip>(R.id.chipAll).setOnClickListener {
            currentFilter = null
            filterAppointments(null)
        }

        findViewById<Chip>(R.id.chipPending).setOnClickListener {
            currentFilter = AppointmentStatus.PENDING
            filterAppointments(AppointmentStatus.PENDING)
        }

        findViewById<Chip>(R.id.chipConfirmed).setOnClickListener {
            currentFilter = AppointmentStatus.CONFIRMED
            filterAppointments(AppointmentStatus.CONFIRMED)
        }

        findViewById<Chip>(R.id.chipCompleted).setOnClickListener {
            currentFilter = AppointmentStatus.COMPLETED
            filterAppointments(AppointmentStatus.COMPLETED)
        }

        // Quick actions
        findViewById<View>(R.id.btnManageSchedule).setOnClickListener {
            startActivity(Intent(this, ScheduleManagementActivity::class.java))
        }

        findViewById<View>(R.id.btnViewStatistics).setOnClickListener {
            // TODO: View statistics
        }

        // Tab layout
        findViewById<TabLayout>(R.id.tabLayout).apply {
            addTab(newTab().setText("Appointments"))
            addTab(newTab().setText("Schedule"))
            addTab(newTab().setText("Statistics"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadAppointments()
                        1 -> startActivity(Intent(this@DentistDashboardActivity, ScheduleManagementActivity::class.java))
                        2 -> { /* TODO: Statistics */ }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun loadDentistProfile() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("dentists").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "Dr. Name"
                    tvDentistName.text = name
                }
            }
    }

    private fun loadAppointments() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("appointments")
            .whereEqualTo("dentistId", userId)
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                allAppointments.clear()
                snapshots?.documents?.forEach { doc ->
                    try {
                    val statusString = doc.getString("status") ?: "PENDING"
                    val status = try {
                        AppointmentStatus.valueOf(statusString)
                    } catch (e: IllegalArgumentException) {
                        AppointmentStatus.PENDING
                    }
                    
                    val appointment = DentistAppointment(
                        id = doc.id,
                        patientId = doc.getString("patientId") ?: "",
                        patientName = doc.getString("patientName") ?: "",
                        patientEmail = doc.getString("patientEmail") ?: "",
                        patientPhone = doc.getString("patientPhone") ?: "",
                        dentistId = doc.getString("dentistId") ?: "",
                        dentistName = doc.getString("dentistName") ?: "",
                        date = doc.getString("date") ?: "",
                        time = doc.getString("time") ?: "",
                        status = status,
                        treatmentType = doc.getString("treatmentType") ?: "",
                        notes = doc.getString("notes") ?: "",
                        treatmentNotes = doc.getString("treatmentNotes") ?: "",
                        prescription = doc.getString("prescription") ?: "",
                        dentalHistory = doc.getString("dentalHistory") ?: ""
                    )
                    allAppointments.add(appointment)
                } catch (e: Exception) {
                    // Skip invalid appointment documents
                }
                }

                updateStatistics()
                filterAppointments(currentFilter)
            }
    }

    private fun filterAppointments(status: AppointmentStatus?) {
        val filtered = if (status == null) {
            allAppointments
        } else {
            allAppointments.filter { it.status == status }
        }

        appointmentAdapter.updateAppointments(filtered)
        
        if (filtered.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            rvAppointments.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            rvAppointments.visibility = View.VISIBLE
        }
    }

    private fun updateStatistics() {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())
        
        val todayAppointments = allAppointments.filter { it.date == today }
        
        tvTotalAppointments.text = todayAppointments.size.toString()
        tvConfirmedAppointments.text = todayAppointments.count { 
            it.status == AppointmentStatus.CONFIRMED 
        }.toString()
        tvPendingAppointments.text = todayAppointments.count { 
            it.status == AppointmentStatus.PENDING 
        }.toString()
    }

    private fun openAppointmentDetails(appointment: DentistAppointment) {
        val intent = Intent(this, DentistAppointmentDetailActivity::class.java)
        intent.putExtra("appointment", appointment)
        startActivity(intent)
    }
}
