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

class AdminDentistAdapter(
    private val onEditClick: (Dentist) -> Unit,
    private val onDeleteClick: (Dentist) -> Unit
) : ListAdapter<Dentist, AdminDentistAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_dentist, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.dentist_name)
        private val specialtyText: TextView = itemView.findViewById(R.id.dentist_specialty)
        private val emailText: TextView = itemView.findViewById(R.id.dentist_email)
        private val experienceText: TextView = itemView.findViewById(R.id.dentist_experience)
        private val editButton: Button = itemView.findViewById(R.id.btn_edit)
        private val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
        
        fun bind(dentist: Dentist) {
            nameText.text = dentist.name
            specialtyText.text = dentist.specialty
            emailText.text = "ID: ${dentist.id}"
            experienceText.text = "${dentist.experience} years experience"
            
            editButton.setOnClickListener { onEditClick(dentist) }
            deleteButton.setOnClickListener { onDeleteClick(dentist) }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Dentist>() {
        override fun areItemsTheSame(oldItem: Dentist, newItem: Dentist) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: Dentist, newItem: Dentist) =
            oldItem == newItem
    }
}
