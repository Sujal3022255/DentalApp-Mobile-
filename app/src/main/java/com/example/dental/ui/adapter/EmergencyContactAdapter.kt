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
import com.example.dental.data.model.EmergencyDentalContact

class EmergencyContactAdapter(
    private val onCallClick: (EmergencyDentalContact) -> Unit
) : ListAdapter<EmergencyDentalContact, EmergencyContactAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency_contact, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneText: TextView = itemView.findViewById(R.id.contact_phone)
        private val availabilityText: TextView = itemView.findViewById(R.id.contact_availability)
        private val callButton: Button = itemView.findViewById(R.id.call_button)
        
        fun bind(contact: EmergencyDentalContact) {
            nameText.text = contact.name
            phoneText.text = contact.phone
            availabilityText.text = if (contact.available247) "Available 24/7" else "Call during business hours"
            
            callButton.setOnClickListener {
                onCallClick(contact)
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<EmergencyDentalContact>() {
        override fun areItemsTheSame(oldItem: EmergencyDentalContact, newItem: EmergencyDentalContact) =
            oldItem.phone == newItem.phone
        
        override fun areContentsTheSame(oldItem: EmergencyDentalContact, newItem: EmergencyDentalContact) =
            oldItem == newItem
    }
}
