package com.example.dental.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dental.data.model.MedicalReport
import com.example.dental.data.model.Prescription
import com.example.dental.data.model.Treatment
import com.example.dental.data.repository.DentalHistoryRepository
import kotlinx.coroutines.launch

class DentalHistoryViewModel : ViewModel() {
    
    private val repository = DentalHistoryRepository()
    
    private val _treatments = MutableLiveData<List<Treatment>>()
    val treatments: LiveData<List<Treatment>> = _treatments
    
    private val _prescriptions = MutableLiveData<List<Prescription>>()
    val prescriptions: LiveData<List<Prescription>> = _prescriptions
    
    private val _reports = MutableLiveData<List<MedicalReport>>()
    val reports: LiveData<List<MedicalReport>> = _reports
    
    private val _downloadStatus = MutableLiveData<Result<String>>()
    val downloadStatus: LiveData<Result<String>> = _downloadStatus
    
    fun loadPatientHistory(patientId: String) {
        viewModelScope.launch {
            repository.treatments.collect { allTreatments ->
                _treatments.value = allTreatments.filter { it.patientId == patientId }
            }
        }
        
        viewModelScope.launch {
            repository.prescriptions.collect { allPrescriptions ->
                _prescriptions.value = allPrescriptions.filter { it.patientId == patientId }
            }
        }
        
        viewModelScope.launch {
            repository.reports.collect { allReports ->
                _reports.value = allReports.filter { it.patientId == patientId }
            }
        }
    }
    
    fun downloadReport(report: MedicalReport) {
        viewModelScope.launch {
            try {
                // Simulate download
                _downloadStatus.value = Result.success("${report.title} downloaded successfully to Downloads folder")
            } catch (e: Exception) {
                _downloadStatus.value = Result.failure(e)
            }
        }
    }
    
    fun downloadPrescription(prescription: Prescription) {
        viewModelScope.launch {
            try {
                // Simulate download
                _downloadStatus.value = Result.success("Prescription for ${prescription.medicationName} downloaded successfully")
            } catch (e: Exception) {
                _downloadStatus.value = Result.failure(e)
            }
        }
    }
    
    fun downloadTreatmentHistory() {
        viewModelScope.launch {
            try {
                // Simulate download of complete treatment history
                _downloadStatus.value = Result.success("Complete treatment history downloaded successfully")
            } catch (e: Exception) {
                _downloadStatus.value = Result.failure(e)
            }
        }
    }
}
