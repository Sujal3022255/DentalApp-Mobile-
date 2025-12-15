package com.example.dental.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.dental.R
import com.example.dental.model.DentistAppointment
import com.example.dental.util.QRCodeGenerator
import java.io.File
import java.io.FileOutputStream

class AppointmentDetailActivity : AppCompatActivity() {

    private lateinit var ivQRCode: ImageView
    private lateinit var tvAppointmentId: TextView
    private lateinit var tvPatientName: TextView
    private lateinit var tvDentistName: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var tvTreatmentType: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnShareQR: Button
    private lateinit var btnReschedule: Button
    private lateinit var btnCancel: Button
    
    private var qrCodeBitmap: Bitmap? = null
    private lateinit var appointment: DentistAppointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)

        // Get appointment data
        appointment = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("appointment", DentistAppointment::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("appointment") as? DentistAppointment
        } ?: run {
            Toast.makeText(this, "Error loading appointment data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializeViews()
        loadAppointmentData()
        generateQRCode()
        setupListeners()
    }

    private fun initializeViews() {
        ivQRCode = findViewById(R.id.ivQRCode)
        tvAppointmentId = findViewById(R.id.tvAppointmentId)
        tvPatientName = findViewById(R.id.tvPatientName)
        tvDentistName = findViewById(R.id.tvDentistName)
        tvDateTime = findViewById(R.id.tvDateTime)
        tvTreatmentType = findViewById(R.id.tvTreatmentType)
        tvStatus = findViewById(R.id.tvStatus)
        btnShareQR = findViewById(R.id.btnShareQR)
        btnReschedule = findViewById(R.id.btnReschedule)
        btnCancel = findViewById(R.id.btnCancel)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun loadAppointmentData() {
        tvAppointmentId.text = appointment.id.take(8).uppercase()
        tvPatientName.text = appointment.patientName
        tvDentistName.text = appointment.dentistName
        tvDateTime.text = "${appointment.date}, ${appointment.time}"
        tvTreatmentType.text = appointment.treatmentType
        tvStatus.text = appointment.status.displayName
        
        // Set status color
        when (appointment.status) {
            com.example.dental.model.AppointmentStatus.CONFIRMED -> {
                tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            }
            com.example.dental.model.AppointmentStatus.PENDING -> {
                tvStatus.setTextColor(getColor(android.R.color.holo_orange_dark))
            }
            com.example.dental.model.AppointmentStatus.COMPLETED -> {
                tvStatus.setTextColor(getColor(android.R.color.holo_blue_dark))
            }
            else -> {
                tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            }
        }
    }

    private fun generateQRCode() {
        val qrData = QRCodeGenerator.generateAppointmentQRData(
            appointmentId = appointment.id,
            patientName = appointment.patientName,
            dentistName = appointment.dentistName,
            date = appointment.date,
            time = appointment.time,
            treatmentType = appointment.treatmentType
        )
        
        qrCodeBitmap = QRCodeGenerator.generateQRCode(qrData, 512)
        ivQRCode.setImageBitmap(qrCodeBitmap)
    }

    private fun setupListeners() {
        btnShareQR.setOnClickListener {
            shareQRCode()
        }

        btnReschedule.setOnClickListener {
            // TODO: Implement reschedule
            Toast.makeText(this, "Reschedule feature coming soon", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            // TODO: Implement cancel
            Toast.makeText(this, "Cancel feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareQRCode() {
        qrCodeBitmap?.let { bitmap ->
            try {
                val cachePath = File(cacheDir, "images")
                cachePath.mkdirs()
                val file = File(cachePath, "appointment_qr.png")
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()

                val uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    file
                )

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, 
                        "Appointment Details:\n" +
                        "ID: ${tvAppointmentId.text}\n" +
                        "Patient: ${tvPatientName.text}\n" +
                        "Dentist: ${tvDentistName.text}\n" +
                        "Date & Time: ${tvDateTime.text}\n" +
                        "Treatment: ${tvTreatmentType.text}")
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error sharing QR code", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "QR code not generated", Toast.LENGTH_SHORT).show()
        }
    }
}
