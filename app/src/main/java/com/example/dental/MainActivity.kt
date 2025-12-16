package com.example.dental

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.view.ProfileActivity
import com.example.dental.view.BrowseDentistsActivity
import com.example.dental.view.MyAppointmentsActivity
import com.example.dental.view.DashboardActivity
import com.example.dental.view.DentalHistoryActivity
import com.example.dental.view.EmergencyActivity
import com.example.dental.view.AdminDashboardActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_main)
            
            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()
            
            // Hide action bar
            supportActionBar?.hide()
            
            // Handle back button with OnBackPressedDispatcher
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
            
            // Navigate to profile
            findViewById<Button>(R.id.profile_button)?.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to browse dentists
            findViewById<Button>(R.id.browse_dentists_button)?.setOnClickListener {
                val intent = Intent(this, BrowseDentistsActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to my appointments
            findViewById<Button>(R.id.my_appointments_button)?.setOnClickListener {
                val intent = Intent(this, MyAppointmentsActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to dashboard
            findViewById<Button>(R.id.dashboard_button)?.setOnClickListener {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to dental history
            findViewById<Button>(R.id.dental_history_button)?.setOnClickListener {
                val intent = Intent(this, DentalHistoryActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to emergency support
            findViewById<Button>(R.id.emergency_button)?.setOnClickListener {
                val intent = Intent(this, EmergencyActivity::class.java)
                startActivity(intent)
            }
            
            // Navigate to admin dashboard
            findViewById<Button>(R.id.admin_dashboard_button)?.setOnClickListener {
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
            }
            
            // Logout button
            findViewById<Button>(R.id.logout_button)?.setOnClickListener {
                showLogoutDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading main activity: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
