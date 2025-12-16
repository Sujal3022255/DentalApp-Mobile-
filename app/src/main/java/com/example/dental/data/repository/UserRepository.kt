package com.example.dental.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dental.data.model.EmergencyContact
import com.example.dental.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create DataStore as a top-level property to ensure singleton
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserRepository(private val context: Context) {
    
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHONE = stringPreferencesKey("user_phone")
        private val USER_DOB = stringPreferencesKey("user_dob")
        private val USER_ADDRESS = stringPreferencesKey("user_address")
        private val USER_PROFILE_PIC = stringPreferencesKey("user_profile_pic")
        private val EMERGENCY_NAME = stringPreferencesKey("emergency_name")
        private val EMERGENCY_RELATIONSHIP = stringPreferencesKey("emergency_relationship")
        private val EMERGENCY_PHONE = stringPreferencesKey("emergency_phone")
        private val EMERGENCY_EMAIL = stringPreferencesKey("emergency_email")
        private val IS_LOGGED_IN = stringPreferencesKey("is_logged_in")
    }
    
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_PHONE] = user.phone
            preferences[USER_DOB] = user.dateOfBirth
            preferences[USER_ADDRESS] = user.address
            preferences[USER_PROFILE_PIC] = user.profilePictureUri
            preferences[IS_LOGGED_IN] = "true"
            
            user.emergencyContact?.let { emergency ->
                preferences[EMERGENCY_NAME] = emergency.name
                preferences[EMERGENCY_RELATIONSHIP] = emergency.relationship
                preferences[EMERGENCY_PHONE] = emergency.phone
                preferences[EMERGENCY_EMAIL] = emergency.email
            }
        }
    }
    
    fun getUser(): Flow<User?> = context.dataStore.data.map { preferences ->
        val isLoggedIn = preferences[IS_LOGGED_IN] ?: "false"
        if (isLoggedIn == "true") {
            val emergencyContact = if (preferences[EMERGENCY_NAME]?.isNotEmpty() == true) {
                EmergencyContact(
                    name = preferences[EMERGENCY_NAME] ?: "",
                    relationship = preferences[EMERGENCY_RELATIONSHIP] ?: "",
                    phone = preferences[EMERGENCY_PHONE] ?: "",
                    email = preferences[EMERGENCY_EMAIL] ?: ""
                )
            } else null
            
            User(
                id = preferences[USER_ID] ?: "",
                name = preferences[USER_NAME] ?: "",
                email = preferences[USER_EMAIL] ?: "",
                phone = preferences[USER_PHONE] ?: "",
                dateOfBirth = preferences[USER_DOB] ?: "",
                address = preferences[USER_ADDRESS] ?: "",
                profilePictureUri = preferences[USER_PROFILE_PIC] ?: "",
                emergencyContact = emergencyContact
            )
        } else {
            null
        }
    }
    
    suspend fun updateProfilePicture(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_PROFILE_PIC] = uri
        }
    }
    
    suspend fun updateEmergencyContact(emergencyContact: EmergencyContact) {
        context.dataStore.edit { preferences ->
            preferences[EMERGENCY_NAME] = emergencyContact.name
            preferences[EMERGENCY_RELATIONSHIP] = emergencyContact.relationship
            preferences[EMERGENCY_PHONE] = emergencyContact.phone
            preferences[EMERGENCY_EMAIL] = emergencyContact.email
        }
    }
    
    suspend fun refreshUser() {
        // DataStore automatically emits updated values through Flow
        // This method is here for explicit refresh calls but Flow handles it automatically
    }
    
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] == "true"
    }
}
