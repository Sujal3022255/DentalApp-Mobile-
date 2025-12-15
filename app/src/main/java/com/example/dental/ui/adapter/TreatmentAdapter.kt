package com.example.dental.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Treatment

class TreatmentAdapter : ListAdapter<Treatment, TreatmentAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treatment, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.treatment_type)
        private val dentistText: TextView = itemView.findViewById(R.id.treatment_dentist)
        private val dateText: TextView = itemView.findViewById(R.id.treatment_date)
        private val descriptionText: TextView = itemView.findViewById(R.id.treatment_description)
        private val costText: TextView = itemView.findViewById(R.id.treatment_cost)
        private val statusText: TextView = itemView.findViewById(R.id.treatment_status)
        private val notesText: TextView = itemView.findViewById(R.id.treatment_notes)
        
        fun bind(treatment: Treatment) {
            typeText.text = treatment.treatmentType
            dentistText.text = "Dr: ${treatment.dentistName}"
            dateText.text = treatment.date
            descriptionText.text = treatment.description
            costText.text = if (treatment.cost > 0) "$${treatment.cost}" else "Consultation"
            statusText.text = treatment.status.name
            notesText.text = treatment.notes
            
            notesText.visibility = if (treatment.notes.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Treatment>() {
        override fun areItemsTheSame(oldItem: Treatment, newItem: Treatment) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: Treatment, newItem: Treatment) =
            oldItem == newItem
    }
}
