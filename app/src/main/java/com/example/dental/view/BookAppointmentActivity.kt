package com.example.dental.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.data.model.Appointment
import com.example.dental.data.model.AppointmentType
import com.example.dental.data.model.Dentist
import com.example.dental.viewmodel.AppointmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class BookAppointmentActivity : AppCompatActivity() {
    
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var dentist: Dentist
    
    private lateinit var dentistNameText: TextView
    private lateinit var dentistSpecialtyText: TextView
    private lateinit var dateInput: EditText
    private lateinit var timeSpinner: Spinner
    private lateinit var appointmentTypeSpinner: Spinner
    private lateinit var notesInput: EditText
    private lateinit var bookButton: Button
    
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Book Appointment"
        
        dentist = intent.getSerializableExtra("DENTIST") as? Dentist ?: run {
            Toast.makeText(this, "Error loading dentist information", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        setupSpinners()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun initViews() {
        dentistNameText = findViewById(R.id.dentist_name_text)
        dentistSpecialtyText = findViewById(R.id.dentist_specialty_text)
        dateInput = findViewById(R.id.appointment_date_input)
        timeSpinner = findViewById(R.id.appointment_time_spinner)
        appointmentTypeSpinner = findViewById(R.id.appointment_type_spinner)
        notesInput = findViewById(R.id.appointment_notes_input)
        bookButton = findViewById(R.id.book_appointment_button)
        
        dentistNameText.text = dentist.name
        dentistSpecialtyText.text = dentist.specialty
    }
    
    private fun setupSpinners() {
        // Time slots spinner
        val timeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dentist.availableTimeSlots
        )
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter
        
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedTime = dentist.availableTimeSlots[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        // Appointment type spinner
        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            AppointmentType.values().map { it.name.replace("_", " ") }
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        appointmentTypeSpinner.adapter = typeAdapter
    }
    
    private fun setupClickListeners() {
        dateInput.setOnClickListener {
            showDatePicker()
        }
        
        bookButton.setOnClickListener {
            bookAppointment()
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
                dateInput.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    
    private fun bookAppointment() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            return
        }
        
        val appointmentType = AppointmentType.values()[appointmentTypeSpinner.selectedItemPosition]
        val notes = notesInput.text.toString().trim()
        
        val appointment = Appointment(
            userId = "USER001", // Replace with actual user ID
            dentistId = dentist.id,
            dentistName = dentist.name,
            dentistSpecialty = dentist.specialty,
            date = selectedDate,
            time = selectedTime,
            type = appointmentType,
            notes = notes
        )
        
        viewModel.bookAppointment(appointment)
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
