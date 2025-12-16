package com.example.dental.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Dentist
import com.example.dental.data.model.User
import com.example.dental.viewmodel.AdminViewModel
import com.google.android.material.tabs.TabLayout

class ManageUsersActivity : AppCompatActivity() {
    
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnAddUser: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Manage Users"
        
        initViews()
        observeViewModel()
        setupClickListeners()
        
        // Load dentists by default
        viewModel.loadDentists()
    }
    
    private fun initViews() {
        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        btnAddUser = findViewById(R.id.btnAddUser)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.dentists.observe(this) { dentists ->
            // Set up dentist adapter
            Toast.makeText(this, "Loaded ${dentists.size} dentists", Toast.LENGTH_SHORT).show()
        }
        
        viewModel.patients.observe(this) { patients ->
            // Set up patient adapter
            Toast.makeText(this, "Loaded ${patients.size} patients", Toast.LENGTH_SHORT).show()
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
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.loadDentists()
                    1 -> viewModel.loadPatients()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        btnAddUser.setOnClickListener {
            showAddDentistDialog()
        }
    }
    
    private fun showAddDentistDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_dentist, null)
        val etName = dialogView.findViewById<EditText>(R.id.etDentistName)
        val etSpecialization = dialogView.findViewById<EditText>(R.id.etSpecialization)
        
        AlertDialog.Builder(this)
            .setTitle("Add Dentist")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val dentist = Dentist(
                    name = etName.text.toString(),
                    specialty = etSpecialization.text.toString()
                )
                viewModel.addDentist(dentist)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
