package com.example.dental

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_splash)
            
            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()
            
            // Delay for 2.5 seconds then check authentication status
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    // Check if user is already logged in
                    val currentUser = auth.currentUser
                    
                    val intent = if (currentUser != null) {
                        // User is logged in, go to MainActivity
                        Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        // User is not logged in, go to LoginActivity
                        Intent(this@SplashActivity, LoginActivity::class.java)
                    }
                    
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // If error, go to login
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2500)
        } catch (e: Exception) {
            e.printStackTrace()
            // If splash fails, go directly to login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
