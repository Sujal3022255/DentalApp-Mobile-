package com.example.dental.data.repository

import com.example.dental.data.model.MedicalReport
import com.example.dental.data.model.Prescription
import com.example.dental.data.model.Treatment
import com.example.dental.data.model.TreatmentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DentalHistoryRepository {
    
    private val _treatments = MutableStateFlow<List<Treatment>>(getSampleTreatments())
    val treatments: StateFlow<List<Treatment>> = _treatments
    
    private val _prescriptions = MutableStateFlow<List<Prescription>>(getSamplePrescriptions())
    val prescriptions: StateFlow<List<Prescription>> = _prescriptions
    
    private val _reports = MutableStateFlow<List<MedicalReport>>(getSampleReports())
    val reports: StateFlow<List<MedicalReport>> = _reports
    
    fun getTreatmentsByPatient(patientId: String): List<Treatment> {
        return _treatments.value.filter { it.patientId == patientId }
    }
    
    fun getPrescriptionsByPatient(patientId: String): List<Prescription> {
        return _prescriptions.value.filter { it.patientId == patientId }
    }
    
    fun getReportsByPatient(patientId: String): List<MedicalReport> {
        return _reports.value.filter { it.patientId == patientId }
    }
    
    private fun getSampleTreatments(): List<Treatment> {
        return listOf(
            Treatment(
                id = "T001",
                patientId = "USER001",
                dentistName = "Dr. Sarah Johnson",
                treatmentType = "Teeth Cleaning",
                description = "Professional dental cleaning and scaling",
                date = "01/11/2025",
                cost = 150.0,
                status = TreatmentStatus.COMPLETED,
                notes = "Patient has good oral hygiene. Recommended bi-annual cleaning."
            ),
            Treatment(
                id = "T002",
                patientId = "USER001",
                dentistName = "Dr. Michael Chen",
                treatmentType = "Cavity Filling",
                description = "Composite filling for upper right molar",
                date = "15/10/2025",
                cost = 280.0,
                status = TreatmentStatus.COMPLETED,
                notes = "Decay removed and tooth restored successfully."
            ),
            Treatment(
                id = "T003",
                patientId = "USER001",
                dentistName = "Dr. Emily Brown",
                treatmentType = "Root Canal",
                description = "Root canal therapy on tooth #14",
                date = "20/09/2025",
                cost = 850.0,
                status = TreatmentStatus.COMPLETED,
                notes = "Three visits completed. Crown placement scheduled."
            ),
            Treatment(
                id = "T004",
                patientId = "USER001",
                dentistName = "Dr. James Wilson",
                treatmentType = "Teeth Whitening",
                description = "Professional teeth whitening treatment",
                date = "05/08/2025",
                cost = 450.0,
                status = TreatmentStatus.COMPLETED,
                notes = "Achieved 3 shades lighter. Maintain with whitening toothpaste."
            ),
            Treatment(
                id = "T005",
                patientId = "USER001",
                dentistName = "Dr. Sarah Johnson",
                treatmentType = "Orthodontic Consultation",
                description = "Initial consultation for braces",
                date = "18/12/2025",
                cost = 0.0,
                status = TreatmentStatus.SCHEDULED,
                notes = "X-rays and impressions to be taken."
            )
        )
    }
    
    private fun getSamplePrescriptions(): List<Prescription> {
        return listOf(
            Prescription(
                id = "P001",
                patientId = "USER001",
                dentistName = "Dr. Michael Chen",
                medicationName = "Amoxicillin",
                dosage = "500mg",
                frequency = "3 times daily",
                duration = "7 days",
                date = "15/10/2025",
                instructions = "Take with food. Complete full course even if symptoms improve.",
                refillsAllowed = 0
            ),
            Prescription(
                id = "P002",
                patientId = "USER001",
                dentistName = "Dr. Emily Brown",
                medicationName = "Ibuprofen",
                dosage = "400mg",
                frequency = "Every 6 hours as needed",
                duration = "5 days",
                date = "20/09/2025",
                instructions = "Take with food or milk. For pain relief after root canal.",
                refillsAllowed = 1
            ),
            Prescription(
                id = "P003",
                patientId = "USER001",
                dentistName = "Dr. Sarah Johnson",
                medicationName = "Chlorhexidine Mouthwash",
                dosage = "10ml",
                frequency = "Twice daily",
                duration = "14 days",
                date = "01/11/2025",
                instructions = "Rinse for 30 seconds after brushing. Do not eat or drink for 30 minutes after use.",
                refillsAllowed = 0
            )
        )
    }
    
    private fun getSampleReports(): List<MedicalReport> {
        return listOf(
            MedicalReport(
                id = "R001",
                patientId = "USER001",
                reportType = "X-Ray Report",
                title = "Dental X-Ray - Full Mouth",
                date = "20/09/2025",
                dentistName = "Dr. Emily Brown",
                findings = "Periapical infection detected in tooth #14. Bone loss minimal. No other abnormalities detected.",
                recommendations = "Root canal therapy recommended for tooth #14. Follow-up X-ray in 6 months.",
                fileUrl = "dental_xray_20092025.pdf"
            ),
            MedicalReport(
                id = "R002",
                patientId = "USER001",
                reportType = "Treatment Summary",
                title = "Root Canal Treatment - Complete",
                date = "25/09/2025",
                dentistName = "Dr. Emily Brown",
                findings = "Root canal therapy completed successfully. All canals cleaned and filled. No complications.",
                recommendations = "Crown placement within 2 weeks. Maintain good oral hygiene.",
                fileUrl = "root_canal_summary_25092025.pdf"
            ),
            MedicalReport(
                id = "R003",
                patientId = "USER001",
                reportType = "Cleaning Report",
                title = "Professional Cleaning Summary",
                date = "01/11/2025",
                dentistName = "Dr. Sarah Johnson",
                findings = "Mild plaque buildup. No cavities detected. Gums healthy with no signs of gingivitis.",
                recommendations = "Continue regular brushing and flossing. Next cleaning in 6 months.",
                fileUrl = "cleaning_report_01112025.pdf"
            ),
            MedicalReport(
                id = "R004",
                patientId = "USER001",
                reportType = "Lab Report",
                title = "Whitening Treatment Results",
                date = "05/08/2025",
                dentistName = "Dr. James Wilson",
                findings = "Teeth whitened by 3 shades (from A3 to A1). No sensitivity reported during treatment.",
                recommendations = "Use whitening toothpaste. Avoid dark beverages for 48 hours.",
                fileUrl = "whitening_results_05082025.pdf"
            )
        )
    }
}
