package com.example.dental.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.DashboardNotification
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val onNotificationClick: (DashboardNotification) -> Unit
) : ListAdapter<DashboardNotification, NotificationAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.notification_title)
        private val message: TextView = itemView.findViewById(R.id.notification_message)
        private val timestamp: TextView = itemView.findViewById(R.id.notification_timestamp)
        private val indicator: View = itemView.findViewById(R.id.unread_indicator)
        
        fun bind(notification: DashboardNotification) {
            title.text = notification.title
            message.text = notification.message
            timestamp.text = formatTimestamp(notification.timestamp)
            indicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE
            
            itemView.setOnClickListener {
                onNotificationClick(notification)
            }
        }
        
        private fun formatTimestamp(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 60000 -> "Just now"
                diff < 3600000 -> "${diff / 60000}m ago"
                diff < 86400000 -> "${diff / 3600000}h ago"
                else -> {
                    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    format.format(Date(timestamp))
                }
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<DashboardNotification>() {
        override fun areItemsTheSame(old: DashboardNotification, new: DashboardNotification) =
            old.id == new.id
        
        override fun areContentsTheSame(old: DashboardNotification, new: DashboardNotification) =
            old == new
    }
}
