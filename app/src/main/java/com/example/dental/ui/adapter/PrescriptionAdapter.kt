package com.example.dental.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Prescription

class PrescriptionAdapter(
    private val onDownloadClick: (Prescription) -> Unit
) : ListAdapter<Prescription, PrescriptionAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prescription, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicationText: TextView = itemView.findViewById(R.id.prescription_medication)
        private val dentistText: TextView = itemView.findViewById(R.id.prescription_dentist)
        private val dateText: TextView = itemView.findViewById(R.id.prescription_date)
        private val dosageText: TextView = itemView.findViewById(R.id.prescription_dosage)
        private val frequencyText: TextView = itemView.findViewById(R.id.prescription_frequency)
        private val durationText: TextView = itemView.findViewById(R.id.prescription_duration)
        private val instructionsText: TextView = itemView.findViewById(R.id.prescription_instructions)
        private val downloadButton: Button = itemView.findViewById(R.id.download_prescription_button)
        
        fun bind(prescription: Prescription) {
            medicationText.text = prescription.medicationName
            dentistText.text = "Prescribed by: ${prescription.dentistName}"
            dateText.text = prescription.date
            dosageText.text = "Dosage: ${prescription.dosage}"
            frequencyText.text = "Frequency: ${prescription.frequency}"
            durationText.text = "Duration: ${prescription.duration}"
            instructionsText.text = prescription.instructions
            
            downloadButton.setOnClickListener {
                onDownloadClick(prescription)
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Prescription>() {
        override fun areItemsTheSame(oldItem: Prescription, newItem: Prescription) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: Prescription, newItem: Prescription) =
            oldItem == newItem
    }
}
