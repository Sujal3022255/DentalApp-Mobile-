package com.example.dental.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.data.model.Appointment
import com.example.dental.viewmodel.AppointmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class RescheduleAppointmentActivity : AppCompatActivity() {
    
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var appointment: Appointment
    
    private lateinit var currentDateText: TextView
    private lateinit var currentTimeText: TextView
    private lateinit var newDateInput: EditText
    private lateinit var newTimeSpinner: Spinner
    private lateinit var rescheduleButton: Button
    
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    
    private val availableTimeSlots = listOf(
        "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
        "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reschedule_appointment)
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Reschedule Appointment"
        
        appointment = intent.getSerializableExtra("APPOINTMENT") as? Appointment ?: run {
            Toast.makeText(this, "Error loading appointment", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        setupSpinner()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun initViews() {
        currentDateText = findViewById(R.id.current_date_text)
        currentTimeText = findViewById(R.id.current_time_text)
        newDateInput = findViewById(R.id.new_date_input)
        newTimeSpinner = findViewById(R.id.new_time_spinner)
        rescheduleButton = findViewById(R.id.reschedule_button)
        
        currentDateText.text = "Current Date: ${appointment.date}"
        currentTimeText.text = "Current Time: ${appointment.time}"
    }
    
    private fun setupSpinner() {
        val timeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            availableTimeSlots
        )
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newTimeSpinner.adapter = timeAdapter
        
        newTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedTime = availableTimeSlots[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun setupClickListeners() {
        newDateInput.setOnClickListener {
            showDatePicker()
        }
        
        rescheduleButton.setOnClickListener {
            rescheduleAppointment()
        }
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)
                newDateInput.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    
    private fun rescheduleAppointment() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a new date", Toast.LENGTH_SHORT).show()
            return
        }
        
        viewModel.rescheduleAppointment(appointment.id, selectedDate, selectedTime)
    }
    
    private fun observeViewModel() {
        viewModel.operationStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
