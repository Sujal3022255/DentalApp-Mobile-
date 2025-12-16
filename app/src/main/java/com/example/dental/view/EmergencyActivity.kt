package com.example.dental.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.EmergencyDentalContact
import com.example.dental.data.model.EmergencyGuidance
import com.example.dental.ui.adapter.EmergencyContactAdapter
import com.example.dental.ui.adapter.EmergencyGuidanceAdapter
import com.example.dental.viewmodel.EmergencyViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmergencyActivity : AppCompatActivity() {
    
    private val viewModel: EmergencyViewModel by viewModels()
    private lateinit var guidanceRecyclerView: RecyclerView
    private lateinit var contactsRecyclerView: RecyclerView
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Phone permission required to make calls", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_emergency)
            
            // Hide action bar
            supportActionBar?.hide()
            
            // Setup close button
            findViewById<ImageView>(R.id.btnCloseEmergency).setOnClickListener {
                finish()
            }
            
            initViews()
            setupAdapters()
            observeViewModel()
            setupEmergencyContactButton()
            
            // Request phone permission
            checkPhonePermission()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading emergency support: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun initViews() {
        guidanceRecyclerView = findViewById(R.id.emergency_guidance_recycler)
        contactsRecyclerView = findViewById(R.id.emergency_contacts_recycler)
        
        guidanceRecyclerView.layoutManager = LinearLayoutManager(this)
        contactsRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupAdapters() {
        val guidanceAdapter = EmergencyGuidanceAdapter { guidance ->
            showGuidanceDetails(guidance)
        }
        guidanceRecyclerView.adapter = guidanceAdapter
        
        val contactAdapter = EmergencyContactAdapter { contact ->
            makeEmergencyCall(contact)
        }
        contactsRecyclerView.adapter = contactAdapter
        
        viewModel.emergencyGuidance.observe(this) { guidance ->
            guidanceAdapter.submitList(guidance)
        }
        
        viewModel.emergencyContacts.observe(this) { contacts ->
            contactAdapter.submitList(contacts)
        }
    }
    
    private fun observeViewModel() {
        viewModel.loadEmergencyData()
    }
    
    private fun setupEmergencyContactButton() {
        findViewById<FloatingActionButton>(R.id.emergency_call_fab)?.setOnClickListener {
            showEmergencyContactDialog()
        }
    }
    
    private fun showEmergencyContactDialog() {
        AlertDialog.Builder(this)
            .setTitle("🚨 Emergency Dental Contact")
            .setMessage("Direct Emergency Line\n\n📞 +977 9817673302\n\nAvailable 24/7 for dental emergencies")
            .setPositiveButton("Call Now") { _, _ ->
                initiateCall("+977 9817673302")
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_menu_call)
            .show()
    }
    
    private fun showGuidanceDetails(guidance: EmergencyGuidance) {
        val intent = Intent(this, EmergencyDetailActivity::class.java)
        intent.putExtra("GUIDANCE", guidance)
        startActivity(intent)
    }
    
    private fun makeEmergencyCall(contact: EmergencyDentalContact) {
        AlertDialog.Builder(this)
            .setTitle("Call ${contact.name}?")
            .setMessage("Phone: ${contact.phone}\n${if (contact.available247) "Available 24/7" else "Check availability"}")
            .setPositiveButton("Call") { _, _ ->
                initiateCall(contact.phone)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun initiateCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
            == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        } else {
            // Use ACTION_DIAL instead (doesn't require permission)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }
    
    private fun checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
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
