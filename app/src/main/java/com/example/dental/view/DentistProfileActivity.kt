package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.data.model.Dentist

class DentistProfileActivity : AppCompatActivity() {
    
    private lateinit var dentist: Dentist
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dentist_profile)
        
        dentist = intent.getSerializableExtra("DENTIST") as? Dentist ?: run {
            finish()
            return
        }
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Dentist Profile"
        
        setupViews()
    }
    
    private fun setupViews() {
        findViewById<TextView>(R.id.profile_name).text = dentist.name
        findViewById<TextView>(R.id.profile_specialty).text = dentist.specialty
        findViewById<TextView>(R.id.profile_experience).text = "${dentist.experience} Years Experience"
        findViewById<TextView>(R.id.profile_rating).text = "${dentist.rating} ⭐"
        
        // Available Days
        val daysText = dentist.availableDays.joinToString(", ")
        findViewById<TextView>(R.id.profile_available_days).text = daysText
        
        // Available Time Slots
        val timeSlotsText = dentist.availableTimeSlots.joinToString("\n")
        findViewById<TextView>(R.id.profile_time_slots).text = timeSlotsText
        
        // Book Appointment Button
        findViewById<Button>(R.id.book_appointment_button).setOnClickListener {
            val intent = Intent(this, BookAppointmentActivity::class.java)
            intent.putExtra("DENTIST", dentist)
            startActivity(intent)
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
