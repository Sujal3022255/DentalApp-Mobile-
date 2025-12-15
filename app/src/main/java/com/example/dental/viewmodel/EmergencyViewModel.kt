package com.example.dental.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dental.data.model.EmergencyDentalContact
import com.example.dental.data.model.EmergencyGuidance
import com.example.dental.data.repository.EmergencyRepository

class EmergencyViewModel : ViewModel() {
    
    private val repository = EmergencyRepository()
    
    private val _emergencyGuidance = MutableLiveData<List<EmergencyGuidance>>()
    val emergencyGuidance: LiveData<List<EmergencyGuidance>> = _emergencyGuidance
    
    private val _emergencyContacts = MutableLiveData<List<EmergencyDentalContact>>()
    val emergencyContacts: LiveData<List<EmergencyDentalContact>> = _emergencyContacts
    
    fun loadEmergencyData() {
        _emergencyGuidance.value = repository.getEmergencyGuidance()
        _emergencyContacts.value = repository.getEmergencyContacts()
    }
}
