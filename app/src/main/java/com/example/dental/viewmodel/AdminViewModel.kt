package com.example.dental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.*
import com.example.dental.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = AdminRepository(application.applicationContext)
    
    private val _appointmentStats = MutableLiveData<AppointmentStats>()
    val appointmentStats: LiveData<AppointmentStats> = _appointmentStats
    
    private val _userStats = MutableLiveData<UserStats>()
    val userStats: LiveData<UserStats> = _userStats
    
    private val _dentists = MutableLiveData<List<Dentist>>()
    val dentists: LiveData<List<Dentist>> = _dentists
    
    private val _patients = MutableLiveData<List<User>>()
    val patients: LiveData<List<User>> = _patients
    
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments
    
    private val _dentalTips = MutableLiveData<List<DentalTip>>()
    val dentalTips: LiveData<List<DentalTip>> = _dentalTips
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _operationStatus = MutableLiveData<Result<String>>()
    val operationStatus: LiveData<Result<String>> = _operationStatus
    
    // ============ Dashboard ============
    
    fun loadDashboardStats() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAppointmentStats().onSuccess {
                _appointmentStats.value = it
            }
            repository.getUserStats().onSuccess {
                _userStats.value = it
            }
            _isLoading.value = false
        }
    }
    
    // ============ User Management ============
    
    fun loadDentists() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllDentists().onSuccess {
                _dentists.value = it
            }.onFailure {
                _operationStatus.value = Result.failure(it)
            }
            _isLoading.value = false
        }
    }
    
    fun addDentist(dentist: Dentist) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.addDentist(dentist)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentists()
            }
            _isLoading.value = false
        }
    }
    
    fun updateDentist(dentist: Dentist) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateDentist(dentist)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentists()
            }
            _isLoading.value = false
        }
    }
    
    fun deleteDentist(dentistId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteDentist(dentistId)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentists()
            }
            _isLoading.value = false
        }
    }
    
    fun loadPatients() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllPatients().onSuccess {
                _patients.value = it
            }.onFailure {
                _operationStatus.value = Result.failure(it)
            }
            _isLoading.value = false
        }
    }
    
    fun updateUserRole(userId: String, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateUserRole(userId, role)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadPatients()
            }
            _isLoading.value = false
        }
    }
    
    // ============ Appointment Management ============
    
    fun loadAllAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllAppointments().onSuccess {
                _appointments.value = it
            }.onFailure {
                _operationStatus.value = Result.failure(it)
            }
            _isLoading.value = false
        }
    }
    
    fun approveAppointment(appointmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.approveAppointment(appointmentId)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadAllAppointments()
            }
            _isLoading.value = false
        }
    }
    
    fun declineAppointment(appointmentId: String, reason: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.declineAppointment(appointmentId, reason)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadAllAppointments()
            }
            _isLoading.value = false
        }
    }
    
    // ============ Content Management ============
    
    fun loadDentalTips() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllDentalTips().onSuccess {
                _dentalTips.value = it
            }.onFailure {
                _operationStatus.value = Result.failure(it)
            }
            _isLoading.value = false
        }
    }
    
    fun addDentalTip(tip: DentalTip) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.addDentalTip(tip)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentalTips()
            }
            _isLoading.value = false
        }
    }
    
    fun updateDentalTip(tip: DentalTip) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateDentalTip(tip)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentalTips()
            }
            _isLoading.value = false
        }
    }
    
    fun deleteDentalTip(tipId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteDentalTip(tipId)
            _operationStatus.value = result
            if (result.isSuccess) {
                loadDentalTips()
            }
            _isLoading.value = false
        }
    }
}
