package com.example.dental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.AuthState
import com.example.dental.data.model.User
import com.example.dental.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UserRepository(application.applicationContext)
    
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            
            // Simulate network delay
            delay(1000)
            
            // Simulate authentication (in real app, call API)
            if (email.isNotEmpty() && password.length >= 6) {
                val user = User(
                    id = UUID.randomUUID().toString(),
                    name = "User",
                    email = email
                )
                userRepository.saveUser(user)
                _authState.value = AuthState(isSuccess = true, user = user)
            } else {
                _authState.value = AuthState(error = "Invalid credentials")
            }
        }
    }
    
    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            
            // Simulate network delay
            delay(1000)
            
            // Simulate registration (in real app, call API)
            if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 6) {
                val user = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email
                )
                userRepository.saveUser(user)
                _authState.value = AuthState(isSuccess = true, user = user)
            } else {
                _authState.value = AuthState(error = "Invalid input")
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _authState.value = AuthState()
        }
    }
}
