package com.example.dental.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.dental.R
import com.example.dental.data.model.User
import com.example.dental.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    
    private val viewModel: ProfileViewModel by viewModels()
    private var profileImageUri: String = ""
    
    private lateinit var profileImage: ImageView
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var dobInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var saveButton: Button
    private lateinit var emergencyButton: Button
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var profileContent: ScrollView
    
    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                profileImageUri = uri.toString()
                Glide.with(this).load(uri).circleCrop().into(profileImage)
                viewModel.updateProfilePicture(profileImageUri)
            }
        }
    }
    
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        // Hide action bar
        supportActionBar?.hide()
        
        initViews()
        observeViewModel()
        setupClickListeners()
        
        // Load user data when activity is created
        viewModel.loadUserData()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload user data when returning to the screen
        viewModel.loadUserData()
    }
    
    private fun initViews() {
        profileImage = findViewById(R.id.profile_image)
        nameInput = findViewById(R.id.profile_name_input)
        emailInput = findViewById(R.id.profile_email_input)
        phoneInput = findViewById(R.id.profile_phone_input)
        dobInput = findViewById(R.id.profile_dob_input)
        addressInput = findViewById(R.id.profile_address_input)
        saveButton = findViewById(R.id.save_profile_button)
        emergencyButton = findViewById(R.id.emergency_contact_button)
        loadingIndicator = findViewById(R.id.loading_indicator)
        profileContent = findViewById(R.id.profile_content)
        
        // Close button handler
        findViewById<ImageView>(R.id.btnCloseProfile).setOnClickListener {
            finish()
        }
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingIndicator.visibility = View.VISIBLE
                profileContent.visibility = View.GONE
            } else {
                loadingIndicator.visibility = View.GONE
                // Only show content if we have user data
                if (viewModel.user.value != null) {
                    profileContent.visibility = View.VISIBLE
                }
            }
        }
        
        viewModel.user.observe(this) { user ->
            user?.let {
                populateFields(it)
                // Ensure content is visible when data is loaded
                loadingIndicator.visibility = View.GONE
                profileContent.visibility = View.VISIBLE
            }
        }
        
        viewModel.updateStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                // Don't clear fields - just show success message
            }
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun clearFormFields() {
        nameInput.setText("")
        emailInput.setText("")
        phoneInput.setText("")
        dobInput.setText("")
        addressInput.setText("")
        profileImageUri = ""
        // Reset profile image to default
        profileImage.setImageResource(R.drawable.img)
    }
    
    private fun populateFields(user: User) {
        nameInput.setText(user.name)
        emailInput.setText(user.email)
        phoneInput.setText(user.phone)
        dobInput.setText(user.dateOfBirth)
        addressInput.setText(user.address)
        profileImageUri = user.profilePictureUri
        
        if (profileImageUri.isNotEmpty()) {
            Glide.with(this).load(Uri.parse(profileImageUri)).circleCrop().into(profileImage)
        } else {
            profileImage.setImageResource(R.drawable.img)
        }
    }
    
    private fun setupClickListeners() {
        profileImage.setOnClickListener {
            checkPermissionAndOpenPicker()
        }
        
        saveButton.setOnClickListener {
            saveProfile()
        }
        
        emergencyButton.setOnClickListener {
            val intent = Intent(this, EmergencyContactActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun checkPermissionAndOpenPicker() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openImagePicker()
            }
            else -> {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }
    
    private fun saveProfile() {
        val currentUser = viewModel.user.value
        val updatedUser = User(
            id = currentUser?.id ?: "",
            name = nameInput.text.toString().trim(),
            email = emailInput.text.toString().trim(),
            phone = phoneInput.text.toString().trim(),
            dateOfBirth = dobInput.text.toString().trim(),
            address = addressInput.text.toString().trim(),
            profilePictureUri = profileImageUri,
            emergencyContact = currentUser?.emergencyContact
        )
        viewModel.updateProfile(updatedUser)
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
