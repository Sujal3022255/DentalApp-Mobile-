package com.example.dental.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Appointment

class DashboardAppointmentAdapter(
    private val onAppointmentClick: (Appointment) -> Unit
) : ListAdapter<Appointment, DashboardAppointmentAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard_appointment, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dentistName: TextView = itemView.findViewById(R.id.dashboard_dentist_name)
        private val appointmentDate: TextView = itemView.findViewById(R.id.dashboard_appointment_date)
        private val appointmentTime: TextView = itemView.findViewById(R.id.dashboard_appointment_time)
        
        fun bind(appointment: Appointment) {
            dentistName.text = appointment.dentistName
            appointmentDate.text = appointment.date
            appointmentTime.text = appointment.time
            
            itemView.setOnClickListener {
                onAppointmentClick(appointment)
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment) =
            oldItem == newItem
    }
}
