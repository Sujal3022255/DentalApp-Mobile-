package com.example.dental.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.data.model.EmergencyGuidance

class EmergencyDetailActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_detail)
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)
        
        val guidance = intent.getSerializableExtra("GUIDANCE") as? EmergencyGuidance
        guidance?.let { displayGuidance(it) }
    }
    
    private fun displayGuidance(guidance: EmergencyGuidance) {
        supportActionBar?.title = guidance.title
        
        findViewById<TextView>(R.id.severity_text).text = 
            "Severity: ${guidance.severity.name}"
        
        findViewById<TextView>(R.id.symptoms_text).text = 
            "Symptoms:\n" + guidance.symptoms.joinToString("\n") { "• $it" }
        
        findViewById<TextView>(R.id.immediate_steps_text).text = 
            "Immediate Steps:\n" + guidance.immediateSteps.joinToString("\n") { "${guidance.immediateSteps.indexOf(it) + 1}. $it" }
        
        findViewById<TextView>(R.id.dos_text).text = 
            "DO:\n" + guidance.dosList.joinToString("\n") { "✓ $it" }
        
        findViewById<TextView>(R.id.donts_text).text = 
            "DON'T:\n" + guidance.dontsList.joinToString("\n") { "✗ $it" }
        
        findViewById<TextView>(R.id.when_to_call_text).text = 
            "When to Call Dentist:\n${guidance.whenToCallDentist}"
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
