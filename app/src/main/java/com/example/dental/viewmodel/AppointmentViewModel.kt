package com.example.dental.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.Appointment
import com.example.dental.data.model.Dentist
import com.example.dental.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel : ViewModel() {
    
    private val repository = AppointmentRepository()
    
    private val _dentists = MutableLiveData<List<Dentist>>()
    val dentists: LiveData<List<Dentist>> = _dentists
    
    private val _upcomingAppointments = MutableLiveData<List<Appointment>>()
    val upcomingAppointments: LiveData<List<Appointment>> = _upcomingAppointments
    
    private val _pastAppointments = MutableLiveData<List<Appointment>>()
    val pastAppointments: LiveData<List<Appointment>> = _pastAppointments
    
    private val _operationStatus = MutableLiveData<Result<String>>()
    val operationStatus: LiveData<Result<String>> = _operationStatus
    
    init {
        loadDentists()
        loadAppointments()
    }
    
    private fun loadDentists() {
        viewModelScope.launch {
            repository.dentists.collect { dentistList ->
                _dentists.value = dentistList
            }
        }
    }
    
    private fun loadAppointments() {
        viewModelScope.launch {
            repository.appointments.collect {
                _upcomingAppointments.value = repository.getUpcomingAppointments()
                _pastAppointments.value = repository.getPastAppointments()
            }
        }
    }
    
    fun bookAppointment(appointment: Appointment) {
        viewModelScope.launch {
            val result = repository.bookAppointment(appointment)
            _operationStatus.value = result
            loadAppointments()
        }
    }
    
    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            val result = repository.cancelAppointment(appointmentId)
            _operationStatus.value = result
            loadAppointments()
        }
    }
    
    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        viewModelScope.launch {
            val result = repository.rescheduleAppointment(appointmentId, newDate, newTime)
            _operationStatus.value = result
            loadAppointments()
        }
    }
}
