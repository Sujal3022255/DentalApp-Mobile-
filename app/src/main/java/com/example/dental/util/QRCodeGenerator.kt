package com.example.dental.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCodeGenerator {
    
    /**
     * Generates a QR code bitmap from the given text
     * @param text The text to encode in the QR code
     * @param size The size of the QR code (width and height)
     * @return Bitmap of the QR code
     */
    fun generateQRCode(text: String, size: Int = 512): Bitmap? {
        return try {
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                put(EncodeHintType.MARGIN, 1)
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
            }
            
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints)
            
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Generates appointment data string for QR code
     */
    fun generateAppointmentQRData(
        appointmentId: String,
        patientName: String,
        dentistName: String,
        date: String,
        time: String,
        treatmentType: String
    ): String {
        return buildString {
            append("APPOINTMENT_ID:$appointmentId\n")
            append("PATIENT:$patientName\n")
            append("DENTIST:$dentistName\n")
            append("DATE:$date\n")
            append("TIME:$time\n")
            append("TREATMENT:$treatmentType")
        }
    }
}
