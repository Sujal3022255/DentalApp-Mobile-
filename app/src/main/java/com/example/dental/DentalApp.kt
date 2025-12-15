package com.example.dental

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class DentalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            // Initialize Firebase
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d("DentalApp", "Firebase initialized successfully")
            } else {
                Log.d("DentalApp", "Firebase already initialized")
            }
        } catch (e: Exception) {
            Log.e("DentalApp", "Firebase initialization error: ${e.message}", e)
            // Manual initialization as fallback
            try {
                val options = FirebaseOptions.Builder()
                    .setApplicationId("1:855476585968:android:589118376a03ba1837c935")
                    .setApiKey("AIzaSyDmdz35GeML_1yZdseiZywq8Yn-tjh0ABY")
                    .setProjectId("dental-clinic-9b316")
                    .setStorageBucket("dental-clinic-9b316.firebasestorage.app")
                    .build()
                FirebaseApp.initializeApp(this, options)
                Log.d("DentalApp", "Firebase initialized manually")
            } catch (e2: Exception) {
                Log.e("DentalApp", "Manual Firebase initialization failed: ${e2.message}", e2)
            }
        }
    }
}
