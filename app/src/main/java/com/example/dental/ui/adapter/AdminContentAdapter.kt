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
import com.example.dental.data.model.DentalTip

class AdminContentAdapter(
    private val onEditClick: (DentalTip) -> Unit,
    private val onDeleteClick: (DentalTip) -> Unit
) : ListAdapter<DentalTip, AdminContentAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_content, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.content_title)
        private val categoryText: TextView = itemView.findViewById(R.id.content_category)
        private val contentText: TextView = itemView.findViewById(R.id.content_text)
        private val statusText: TextView = itemView.findViewById(R.id.content_status)
        private val editButton: Button = itemView.findViewById(R.id.btn_edit)
        private val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
        
        fun bind(tip: DentalTip) {
            titleText.text = tip.title
            categoryText.text = "Category: ${tip.category}"
            contentText.text = if (tip.content.length > 100) {
                tip.content.substring(0, 100) + "..."
            } else {
                tip.content
            }
            statusText.text = if (tip.isActive) "Active" else "Inactive"
            statusText.setTextColor(
                itemView.context.getColor(
                    if (tip.isActive) R.color.green else R.color.dental_gray
                )
            )
            
            editButton.setOnClickListener { onEditClick(tip) }
            deleteButton.setOnClickListener { onDeleteClick(tip) }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<DentalTip>() {
        override fun areItemsTheSame(oldItem: DentalTip, newItem: DentalTip) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: DentalTip, newItem: DentalTip) =
            oldItem == newItem
    }
}
