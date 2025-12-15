package com.example.dental.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.model.DentistSchedule
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ScheduleManagementActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var calendarView: CalendarView
    private lateinit var tvSelectedDate: TextView
    private lateinit var switchAvailable: SwitchMaterial
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var chipGroupDuration: ChipGroup
    
    private var selectedDate: String = ""
    private var startTime: String = "09:00 AM"
    private var endTime: String = "06:00 PM"
    private var slotDuration: Int = 30
    private val blockedDates = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_management)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        initializeViews()
        setupListeners()
        loadScheduleData()
    }

    private fun initializeViews() {
        calendarView = findViewById(R.id.calendarView)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        switchAvailable = findViewById(R.id.switchAvailable)
        btnStartTime = findViewById(R.id.btnStartTime)
        btnEndTime = findViewById(R.id.btnEndTime)
        chipGroupDuration = findViewById(R.id.chipGroupDuration)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Set default selected date
        val calendar = Calendar.getInstance()
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        updateSelectedDateText()
    }

    private fun setupListeners() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            updateSelectedDateText()
            checkIfDateBlocked()
        }

        btnStartTime.setOnClickListener {
            showTimePicker(true)
        }

        btnEndTime.setOnClickListener {
            showTimePicker(false)
        }

        chipGroupDuration.setOnCheckedStateChangeListener { _, checkedIds ->
            slotDuration = when {
                checkedIds.contains(R.id.chip15) -> 15
                checkedIds.contains(R.id.chip30) -> 30
                checkedIds.contains(R.id.chip60) -> 60
                else -> 30
            }
        }

        findViewById<Button>(R.id.btnBlockDate).setOnClickListener {
            blockSelectedDate()
        }

        findViewById<Button>(R.id.btnSaveTimeSlots).setOnClickListener {
            saveTimeSlots()
        }

        findViewById<Button>(R.id.btnSaveWeeklySchedule).setOnClickListener {
            saveWeeklySchedule()
        }
    }

    private fun updateSelectedDateText() {
        tvSelectedDate.text = "Selected: $selectedDate"
    }

    private fun checkIfDateBlocked() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("dentistSchedules")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val blocked = document.get("blockedDates") as? List<String> ?: emptyList()
                    switchAvailable.isChecked = !blocked.contains(selectedDate)
                }
            }
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour >= 12) "PM" else "AM"
                val hour12 = if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour
                val timeString = String.format("%02d:%02d %s", hour12, selectedMinute, amPm)
                
                if (isStartTime) {
                    startTime = timeString
                    btnStartTime.text = timeString
                } else {
                    endTime = timeString
                    btnEndTime.text = timeString
                }
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun blockSelectedDate() {
        val userId = auth.currentUser?.uid ?: return

        AlertDialog.Builder(this)
            .setTitle("Block Date")
            .setMessage("Are you sure you want to block $selectedDate?")
            .setPositiveButton("Block") { _, _ ->
                blockedDates.add(selectedDate)
                
                db.collection("dentistSchedules")
                    .document(userId)
                    .update("blockedDates", blockedDates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Date blocked successfully", Toast.LENGTH_SHORT).show()
                        switchAvailable.isChecked = false
                    }
                    .addOnFailureListener { e ->
                        // Try to create document if it doesn't exist
                        val scheduleData = hashMapOf(
                            "dentistId" to userId,
                            "blockedDates" to blockedDates
                        )
                        db.collection("dentistSchedules")
                            .document(userId)
                            .set(scheduleData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Date blocked successfully", Toast.LENGTH_SHORT).show()
                                switchAvailable.isChecked = false
                            }
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveTimeSlots() {
        val userId = auth.currentUser?.uid ?: return

        val timeSlotData = hashMapOf(
            "dentistId" to userId,
            "startTime" to startTime,
            "endTime" to endTime,
            "slotDuration" to slotDuration,
            "updatedAt" to System.currentTimeMillis()
        )

        db.collection("dentistSchedules")
            .document(userId)
            .set(timeSlotData, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Time slots saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveWeeklySchedule() {
        val userId = auth.currentUser?.uid ?: return

        val availableDays = mutableListOf<String>()
        
        if (findViewById<SwitchMaterial>(R.id.switchMonday).isChecked) availableDays.add("Monday")
        if (findViewById<SwitchMaterial>(R.id.switchTuesday).isChecked) availableDays.add("Tuesday")
        if (findViewById<SwitchMaterial>(R.id.switchWednesday).isChecked) availableDays.add("Wednesday")
        if (findViewById<SwitchMaterial>(R.id.switchThursday).isChecked) availableDays.add("Thursday")
        if (findViewById<SwitchMaterial>(R.id.switchFriday).isChecked) availableDays.add("Friday")
        if (findViewById<SwitchMaterial>(R.id.switchSaturday).isChecked) availableDays.add("Saturday")
        if (findViewById<SwitchMaterial>(R.id.switchSunday).isChecked) availableDays.add("Sunday")

        val scheduleData = hashMapOf(
            "dentistId" to userId,
            "availableDays" to availableDays,
            "updatedAt" to System.currentTimeMillis()
        )

        db.collection("dentistSchedules")
            .document(userId)
            .set(scheduleData, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Weekly schedule saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        // Also update dentist profile
        db.collection("dentists")
            .document(userId)
            .update("availableDays", availableDays)
    }

    private fun loadScheduleData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("dentistSchedules")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Load time slots
                    startTime = document.getString("startTime") ?: "09:00 AM"
                    endTime = document.getString("endTime") ?: "06:00 PM"
                    slotDuration = document.getLong("slotDuration")?.toInt() ?: 30
                    
                    btnStartTime.text = startTime
                    btnEndTime.text = endTime
                    
                    when (slotDuration) {
                        15 -> chipGroupDuration.check(R.id.chip15)
                        30 -> chipGroupDuration.check(R.id.chip30)
                        60 -> chipGroupDuration.check(R.id.chip60)
                    }

                    // Load blocked dates
                    val blocked = document.get("blockedDates") as? List<String> ?: emptyList()
                    blockedDates.clear()
                    blockedDates.addAll(blocked)
                    
                    // Load available days
                    val availableDays = document.get("availableDays") as? List<String> ?: emptyList()
                    findViewById<SwitchMaterial>(R.id.switchMonday).isChecked = availableDays.contains("Monday")
                    findViewById<SwitchMaterial>(R.id.switchTuesday).isChecked = availableDays.contains("Tuesday")
                    findViewById<SwitchMaterial>(R.id.switchWednesday).isChecked = availableDays.contains("Wednesday")
                    findViewById<SwitchMaterial>(R.id.switchThursday).isChecked = availableDays.contains("Thursday")
                    findViewById<SwitchMaterial>(R.id.switchFriday).isChecked = availableDays.contains("Friday")
                    findViewById<SwitchMaterial>(R.id.switchSaturday).isChecked = availableDays.contains("Saturday")
                    findViewById<SwitchMaterial>(R.id.switchSunday).isChecked = availableDays.contains("Sunday")
                }
            }
    }
}
