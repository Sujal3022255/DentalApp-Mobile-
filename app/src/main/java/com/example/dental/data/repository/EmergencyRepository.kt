package com.example.dental.data.repository

import com.example.dental.data.model.ContactType
import com.example.dental.data.model.EmergencyDentalContact
import com.example.dental.data.model.EmergencyGuidance
import com.example.dental.data.model.EmergencySeverity

class EmergencyRepository {
    
    fun getEmergencyGuidance(): List<EmergencyGuidance> {
        return listOf(
            EmergencyGuidance(
                id = "E001",
                title = "Severe Toothache",
                severity = EmergencySeverity.HIGH,
                symptoms = listOf(
                    "Throbbing or persistent pain",
                    "Pain when chewing or touching tooth",
                    "Sensitivity to hot or cold",
                    "Swelling around tooth or jaw",
                    "Fever or headache"
                ),
                immediateSteps = listOf(
                    "Rinse mouth with warm salt water",
                    "Use dental floss to remove any trapped food",
                    "Take over-the-counter pain reliever (ibuprofen or acetaminophen)",
                    "Apply cold compress to outside of cheek",
                    "Keep head elevated, even while sleeping"
                ),
                dosList = listOf(
                    "Rinse with warm salt water every 2-3 hours",
                    "Take pain medication as directed",
                    "Eat soft foods on opposite side",
                    "Keep the area clean"
                ),
                dontsList = listOf(
                    "DON'T apply aspirin directly to tooth or gums",
                    "DON'T use extremely hot or cold foods",
                    "DON'T ignore the pain",
                    "DON'T delay dental treatment"
                ),
                whenToCallDentist = "Call immediately if pain is severe, accompanied by fever, swelling, or lasts more than 1-2 days",
                icon = "toothache"
            ),
            EmergencyGuidance(
                id = "E002",
                title = "Knocked Out Tooth",
                severity = EmergencySeverity.CRITICAL,
                symptoms = listOf(
                    "Tooth completely out of socket",
                    "Bleeding from socket",
                    "Pain and swelling"
                ),
                immediateSteps = listOf(
                    "Find the tooth immediately",
                    "Hold tooth by the crown (top), NOT the root",
                    "Rinse tooth gently with water if dirty (don't scrub)",
                    "Try to reinsert tooth in socket if possible",
                    "If can't reinsert, place in milk or saliva",
                    "Get to dentist within 30 minutes - TIME IS CRITICAL!"
                ),
                dosList = listOf(
                    "Handle tooth by crown only",
                    "Keep tooth moist at all times",
                    "Get to dentist immediately",
                    "Bite on clean gauze to stop bleeding"
                ),
                dontsList = listOf(
                    "DON'T touch or scrub the root",
                    "DON'T let tooth dry out",
                    "DON'T wrap in tissue or cloth",
                    "DON'T delay - every minute counts!"
                ),
                whenToCallDentist = "CALL IMMEDIATELY! This is a dental emergency. Time is critical for saving the tooth.",
                icon = "knocked_out"
            ),
            EmergencyGuidance(
                id = "E003",
                title = "Broken or Chipped Tooth",
                severity = EmergencySeverity.MODERATE,
                symptoms = listOf(
                    "Visible crack or chip in tooth",
                    "Sharp edges causing discomfort",
                    "Pain when eating or drinking",
                    "Sensitivity"
                ),
                immediateSteps = listOf(
                    "Rinse mouth with warm water",
                    "Save any pieces of the tooth",
                    "Apply gauze if bleeding",
                    "Use cold compress for swelling",
                    "Cover sharp edge with dental wax or sugarless gum"
                ),
                dosList = listOf(
                    "Save all tooth fragments",
                    "Eat soft foods",
                    "Cover sharp edges",
                    "Schedule dental appointment soon"
                ),
                dontsList = listOf(
                    "DON'T chew on that tooth",
                    "DON'T use the tooth to bite hard foods",
                    "DON'T ignore even small chips"
                ),
                whenToCallDentist = "Call within 24 hours. Immediate care if there's severe pain or bleeding.",
                icon = "broken_tooth"
            ),
            EmergencyGuidance(
                id = "E004",
                title = "Bleeding Gums or Mouth",
                severity = EmergencySeverity.MODERATE,
                symptoms = listOf(
                    "Continuous bleeding from gums",
                    "Blood when brushing or flossing",
                    "Bleeding after dental procedure",
                    "Mouth injury with bleeding"
                ),
                immediateSteps = listOf(
                    "Rinse mouth with cold water",
                    "Apply pressure with clean gauze for 10-15 minutes",
                    "Use a cold compress on outside of mouth",
                    "Sit upright or elevate head",
                    "Avoid hot foods and drinks"
                ),
                dosList = listOf(
                    "Apply firm, steady pressure",
                    "Use clean gauze or cloth",
                    "Keep head elevated",
                    "Stay calm and still"
                ),
                dontsList = listOf(
                    "DON'T rinse mouth vigorously",
                    "DON'T use hot water",
                    "DON'T smoke",
                    "DON'T drink through a straw"
                ),
                whenToCallDentist = "Call if bleeding doesn't stop after 15-20 minutes of pressure, or if caused by injury.",
                icon = "bleeding"
            ),
            EmergencyGuidance(
                id = "E005",
                title = "Abscess or Swelling",
                severity = EmergencySeverity.HIGH,
                symptoms = listOf(
                    "Painful swelling in gums",
                    "Pimple-like bump on gums",
                    "Fever",
                    "Bad taste in mouth",
                    "Swollen lymph nodes"
                ),
                immediateSteps = listOf(
                    "Rinse with warm salt water several times",
                    "Take over-the-counter pain reliever",
                    "Apply cold compress to reduce swelling",
                    "Stay hydrated",
                    "Contact dentist immediately"
                ),
                dosList = listOf(
                    "Rinse frequently with salt water",
                    "Take pain medication as directed",
                    "Drink plenty of fluids",
                    "Get dental care as soon as possible"
                ),
                dontsList = listOf(
                    "DON'T try to pop or drain the abscess",
                    "DON'T ignore the symptoms",
                    "DON'T delay treatment",
                    "DON'T apply heat"
                ),
                whenToCallDentist = "Call immediately. Dental abscesses can spread and become serious infections.",
                icon = "abscess"
            ),
            EmergencyGuidance(
                id = "E006",
                title = "Lost Filling or Crown",
                severity = EmergencySeverity.LOW,
                symptoms = listOf(
                    "Filling or crown came out",
                    "Sensitivity in exposed tooth",
                    "Discomfort when eating"
                ),
                immediateSteps = listOf(
                    "Save the filling or crown",
                    "Clean the crown if possible",
                    "Try to reposition crown with dental cement or toothpaste",
                    "Avoid chewing on that side",
                    "Cover exposed area with dental wax"
                ),
                dosList = listOf(
                    "Keep filling/crown safe",
                    "Maintain good oral hygiene",
                    "Eat soft foods",
                    "Schedule appointment promptly"
                ),
                dontsList = listOf(
                    "DON'T use super glue on crown",
                    "DON'T chew on exposed tooth",
                    "DON'T eat sticky or hard foods"
                ),
                whenToCallDentist = "Call within 1-2 days to schedule repair. Can wait for regular business hours.",
                icon = "crown"
            )
        )
    }
    
    fun getEmergencyContacts(): List<EmergencyDentalContact> {
        return listOf(
            EmergencyDentalContact(
                name = "24/7 Emergency Dental",
                phone = "1-800-DENTIST",
                type = ContactType.EMERGENCY_DENTAL,
                available247 = true
            ),
            EmergencyDentalContact(
                name = "Dr. Sarah Johnson",
                phone = "+1-555-0123",
                type = ContactType.DENTIST,
                available247 = false
            ),
            EmergencyDentalContact(
                name = "Local Emergency Room",
                phone = "911",
                type = ContactType.HOSPITAL,
                available247 = true
            ),
            EmergencyDentalContact(
                name = "Poison Control",
                phone = "1-800-222-1222",
                type = ContactType.POISON_CONTROL,
                available247 = true
            )
        )
    }
}
