package com.example.dental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.EmergencyContact
import com.example.dental.data.model.User
import com.example.dental.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UserRepository(application.applicationContext)
    
    val user: LiveData<User?> = userRepository.getUser().asLiveData()
    
    private val _updateStatus = MutableLiveData<Result<String>>()
    val updateStatus: LiveData<Result<String>> = _updateStatus
    
    fun loadUserData() {
        // Trigger a reload of user data from repository
        viewModelScope.launch {
            try {
                userRepository.refreshUser()
            } catch (e: Exception) {
                // Silent fail - the LiveData will continue observing
            }
        }
    }
    
    fun updateProfile(user: User) {
        viewModelScope.launch {
            try {
                userRepository.saveUser(user)
                _updateStatus.value = Result.success("Profile updated successfully")
            } catch (e: Exception) {
                _updateStatus.value = Result.failure(e)
            }
        }
    }
    
    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            try {
                userRepository.updateProfilePicture(uri)
                _updateStatus.value = Result.success("Profile picture updated")
            } catch (e: Exception) {
                _updateStatus.value = Result.failure(e)
            }
        }
    }
    
    fun updateEmergencyContact(emergencyContact: EmergencyContact) {
        viewModelScope.launch {
            try {
                userRepository.updateEmergencyContact(emergencyContact)
                _updateStatus.value = Result.success("Emergency contact updated")
            } catch (e: Exception) {
                _updateStatus.value = Result.failure(e)
            }
        }
    }
}
