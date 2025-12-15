package com.example.dental.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.model.AppointmentStatus
import com.example.dental.model.DentistAppointment

class DentistAppointmentAdapter(
    private var appointments: MutableList<DentistAppointment>,
    private val onItemClick: (DentistAppointment) -> Unit
) : RecyclerView.Adapter<DentistAppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPatientName: TextView = view.findViewById(R.id.tvPatientName)
        val tvPatientPhone: TextView = view.findViewById(R.id.tvPatientPhone)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvAppointmentDate: TextView = view.findViewById(R.id.tvAppointmentDate)
        val tvAppointmentTime: TextView = view.findViewById(R.id.tvAppointmentTime)
        val tvTreatmentType: TextView = view.findViewById(R.id.tvTreatmentType)
        val btnViewDetails: Button = view.findViewById(R.id.btnViewDetails)
        val btnUpdateStatus: Button = view.findViewById(R.id.btnUpdateStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dentist_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        
        holder.tvPatientName.text = appointment.patientName
        holder.tvPatientPhone.text = appointment.patientPhone
        holder.tvAppointmentDate.text = appointment.date
        holder.tvAppointmentTime.text = appointment.time
        holder.tvTreatmentType.text = appointment.treatmentType
        
        // Set status with color
        holder.tvStatus.text = appointment.status.displayName
        val statusBackground = when (appointment.status) {
            AppointmentStatus.PENDING -> R.drawable.bg_status_pending
            AppointmentStatus.CONFIRMED -> R.drawable.bg_status_confirmed
            AppointmentStatus.COMPLETED -> R.drawable.bg_status_completed
            AppointmentStatus.CANCELLED -> R.drawable.bg_status_cancelled
            AppointmentStatus.NO_SHOW -> R.drawable.bg_status_cancelled
        }
        holder.tvStatus.setBackgroundResource(statusBackground)
        
        holder.btnViewDetails.setOnClickListener {
            onItemClick(appointment)
        }
        
        holder.btnUpdateStatus.setOnClickListener {
            // Open detail activity for status update
            val context = holder.itemView.context
            val intent = android.content.Intent(context, com.example.dental.view.DentistAppointmentDetailActivity::class.java)
            intent.putExtra("appointment", appointment)
            context.startActivity(intent)
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(appointment)
        }
    }

    override fun getItemCount() = appointments.size

    fun updateAppointments(newAppointments: List<DentistAppointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }
}
