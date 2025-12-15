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
import com.example.dental.data.model.Dentist

class DentistAdapter(
    private val onDentistClick: (Dentist) -> Unit,
    private val onBookClick: (Dentist) -> Unit
) : ListAdapter<Dentist, DentistAdapter.DentistViewHolder>(DentistDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DentistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dentist, parent, false)
        return DentistViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DentistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class DentistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.dentist_name)
        private val specialtyText: TextView = itemView.findViewById(R.id.dentist_specialty)
        private val experienceText: TextView = itemView.findViewById(R.id.dentist_experience)
        private val ratingText: TextView = itemView.findViewById(R.id.dentist_rating)
        private val bookButton: Button = itemView.findViewById(R.id.book_dentist_button)
        
        fun bind(dentist: Dentist) {
            nameText.text = dentist.name
            specialtyText.text = dentist.specialty
            experienceText.text = "${dentist.experience} years experience"
            ratingText.text = "⭐ ${dentist.rating}"
            
            bookButton.setOnClickListener {
                onBookClick(dentist)
            }
            
            itemView.setOnClickListener {
                onDentistClick(dentist)
            }
        }
    }
    
    private class DentistDiffCallback : DiffUtil.ItemCallback<Dentist>() {
        override fun areItemsTheSame(oldItem: Dentist, newItem: Dentist): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Dentist, newItem: Dentist): Boolean {
            return oldItem == newItem
        }
    }
}
