package com.example.dental.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.EmergencyGuidance

class EmergencyGuidanceAdapter(
    private val onItemClick: (EmergencyGuidance) -> Unit
) : ListAdapter<EmergencyGuidance, EmergencyGuidanceAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency_guidance, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.emergency_title)
        private val severityText: TextView = itemView.findViewById(R.id.emergency_severity)
        private val descriptionText: TextView = itemView.findViewById(R.id.emergency_description)
        
        fun bind(guidance: EmergencyGuidance) {
            titleText.text = guidance.title
            severityText.text = guidance.severity.name
            descriptionText.text = "Tap for detailed guidance"
            
            // Set severity color
            val colorRes = when (guidance.severity) {
                com.example.dental.data.model.EmergencySeverity.CRITICAL -> R.color.red
                com.example.dental.data.model.EmergencySeverity.HIGH -> R.color.orange
                com.example.dental.data.model.EmergencySeverity.MODERATE -> R.color.yellow
                com.example.dental.data.model.EmergencySeverity.LOW -> R.color.green
            }
            severityText.setTextColor(itemView.context.getColor(colorRes))
            
            itemView.setOnClickListener {
                onItemClick(guidance)
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<EmergencyGuidance>() {
        override fun areItemsTheSame(oldItem: EmergencyGuidance, newItem: EmergencyGuidance) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: EmergencyGuidance, newItem: EmergencyGuidance) =
            oldItem == newItem
    }
}
