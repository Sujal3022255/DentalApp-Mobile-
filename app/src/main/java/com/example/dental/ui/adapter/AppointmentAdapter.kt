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
import com.example.dental.data.model.Appointment
import com.example.dental.data.model.AppointmentStatus

class AppointmentAdapter(
    private val onCancelClick: (Appointment) -> Unit,
    private val onRescheduleClick: (Appointment) -> Unit
) : ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(AppointmentDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dentistNameText: TextView = itemView.findViewById(R.id.appointment_dentist_name)
        private val specialtyText: TextView = itemView.findViewById(R.id.appointment_specialty)
        private val dateText: TextView = itemView.findViewById(R.id.appointment_date)
        private val timeText: TextView = itemView.findViewById(R.id.appointment_time)
        private val typeText: TextView = itemView.findViewById(R.id.appointment_type)
        private val statusText: TextView = itemView.findViewById(R.id.appointment_status)
        private val cancelButton: Button = itemView.findViewById(R.id.cancel_appointment_button)
        private val rescheduleButton: Button = itemView.findViewById(R.id.reschedule_appointment_button)
        
        fun bind(appointment: Appointment) {
            dentistNameText.text = appointment.dentistName
            specialtyText.text = appointment.dentistSpecialty
            dateText.text = "Date: ${appointment.date}"
            timeText.text = "Time: ${appointment.time}"
            typeText.text = "Type: ${appointment.type.name.replace("_", " ")}"
            statusText.text = "Status: ${appointment.status.name}"
            
            // Show/hide action buttons based on status
            val isUpcoming = appointment.status == AppointmentStatus.SCHEDULED || 
                             appointment.status == AppointmentStatus.RESCHEDULED
            
            cancelButton.visibility = if (isUpcoming) View.VISIBLE else View.GONE
            rescheduleButton.visibility = if (isUpcoming) View.VISIBLE else View.GONE
            
            cancelButton.setOnClickListener {
                onCancelClick(appointment)
            }
            
            rescheduleButton.setOnClickListener {
                onRescheduleClick(appointment)
            }
        }
    }
    
    private class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem == newItem
        }
    }
}
