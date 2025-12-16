package com.example.dental.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.data.model.EmergencyContact
import com.example.dental.viewmodel.ProfileViewModel

class EmergencyContactActivity : AppCompatActivity() {
    
    private val viewModel: ProfileViewModel by viewModels()
    
    private lateinit var nameInput: EditText
    private lateinit var relationshipInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)
        
        // Hide action bar
        supportActionBar?.hide()
        
        initViews()
        observeViewModel()
        setupClickListeners()
    }
    
    private fun initViews() {
        nameInput = findViewById(R.id.emergency_name_input)
        relationshipInput = findViewById(R.id.emergency_relationship_input)
        phoneInput = findViewById(R.id.emergency_phone_input)
        emailInput = findViewById(R.id.emergency_email_input)
        saveButton = findViewById(R.id.save_emergency_button)
        
        // Close button handler
        findViewById<ImageView>(R.id.btnCloseEmergency).setOnClickListener {
            finish()
        }
    }
    
    private fun observeViewModel() {
        viewModel.user.observe(this) { user ->
            user?.emergencyContact?.let { emergency ->
                nameInput.setText(emergency.name)
                relationshipInput.setText(emergency.relationship)
                phoneInput.setText(emergency.phone)
                emailInput.setText(emergency.email)
            }
        }
        
        viewModel.updateStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            saveEmergencyContact()
        }
    }
    
    private fun saveEmergencyContact() {
        val name = nameInput.text.toString().trim()
        val relationship = relationshipInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        
        if (name.isEmpty() || relationship.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val emergencyContact = EmergencyContact(
            name = name,
            relationship = relationship,
            phone = phone,
            email = email
        )
        
        viewModel.updateEmergencyContact(emergencyContact)
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
