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

class AdminAppointmentAdapter(
    private val onApproveClick: (Appointment) -> Unit,
    private val onDeclineClick: (Appointment) -> Unit
) : ListAdapter<Appointment, AdminAppointmentAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_appointment, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val patientName: TextView = itemView.findViewById(R.id.patient_name)
        private val dentistName: TextView = itemView.findViewById(R.id.dentist_name)
        private val dateTime: TextView = itemView.findViewById(R.id.appointment_datetime)
        private val treatmentType: TextView = itemView.findViewById(R.id.treatment_type)
        private val statusText: TextView = itemView.findViewById(R.id.status_text)
        private val approveButton: Button = itemView.findViewById(R.id.btn_approve)
        private val declineButton: Button = itemView.findViewById(R.id.btn_decline)
        
        fun bind(appointment: Appointment) {
            patientName.text = "Patient ID: ${appointment.userId}"
            dentistName.text = "Dentist: ${appointment.dentistName}"
            dateTime.text = "${appointment.date} at ${appointment.time}"
            treatmentType.text = appointment.type.name
            statusText.text = "Status: ${appointment.status.name}"
            
            // Show approve/decline buttons only for scheduled appointments
            val isPending = appointment.status == AppointmentStatus.SCHEDULED
            approveButton.visibility = if (isPending) View.VISIBLE else View.GONE
            declineButton.visibility = if (isPending) View.VISIBLE else View.GONE
            
            approveButton.setOnClickListener { onApproveClick(appointment) }
            declineButton.setOnClickListener { onDeclineClick(appointment) }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment) =
            oldItem == newItem
    }
}
