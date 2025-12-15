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
import com.example.dental.data.model.MedicalReport

class MedicalReportAdapter(
    private val onDownloadClick: (MedicalReport) -> Unit
) : ListAdapter<MedicalReport, MedicalReportAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medical_report, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.report_title)
        private val typeText: TextView = itemView.findViewById(R.id.report_type)
        private val dentistText: TextView = itemView.findViewById(R.id.report_dentist)
        private val dateText: TextView = itemView.findViewById(R.id.report_date)
        private val findingsText: TextView = itemView.findViewById(R.id.report_findings)
        private val recommendationsText: TextView = itemView.findViewById(R.id.report_recommendations)
        private val downloadButton: Button = itemView.findViewById(R.id.download_report_button)
        
        fun bind(report: MedicalReport) {
            titleText.text = report.title
            typeText.text = report.reportType
            dentistText.text = "By: ${report.dentistName}"
            dateText.text = report.date
            findingsText.text = "Findings: ${report.findings}"
            recommendationsText.text = "Recommendations: ${report.recommendations}"
            
            downloadButton.setOnClickListener {
                onDownloadClick(report)
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<MedicalReport>() {
        override fun areItemsTheSame(oldItem: MedicalReport, newItem: MedicalReport) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: MedicalReport, newItem: MedicalReport) =
            oldItem == newItem
    }
}
