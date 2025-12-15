package com.example.dental.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.MedicalReport
import com.example.dental.data.model.Prescription
import com.example.dental.data.model.Treatment
import com.example.dental.ui.adapter.MedicalReportAdapter
import com.example.dental.ui.adapter.PrescriptionAdapter
import com.example.dental.ui.adapter.TreatmentAdapter
import com.example.dental.viewmodel.DentalHistoryViewModel
import com.google.android.material.tabs.TabLayout

class DentalHistoryActivity : AppCompatActivity() {
    
    private val viewModel: DentalHistoryViewModel by viewModels()
    private lateinit var topTabLayout: TabLayout
    private lateinit var historyTabLayout: TabLayout
    private lateinit var contentRecyclerView: RecyclerView
    private lateinit var downloadAllButton: Button
    private lateinit var btnClose: ImageView
    
    private lateinit var treatmentAdapter: TreatmentAdapter
    private lateinit var prescriptionAdapter: PrescriptionAdapter
    private lateinit var reportAdapter: MedicalReportAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dental_history)
        
        // Hide action bar - we're using custom header
        supportActionBar?.hide()
        
        initViews()
        setupTopTabs()
        setupHistoryTabs()
        setupAdapters()
        observeViewModel()
        
        // Load patient history
        viewModel.loadPatientHistory("USER001")
    }
    
    private fun initViews() {
        topTabLayout = findViewById(R.id.top_tab_layout)
        historyTabLayout = findViewById(R.id.history_tab_layout)
        contentRecyclerView = findViewById(R.id.history_content_recycler)
        downloadAllButton = findViewById(R.id.download_all_button)
        btnClose = findViewById(R.id.btnClose)
        
        contentRecyclerView.layoutManager = LinearLayoutManager(this)
        
        btnClose.setOnClickListener {
            finish()
        }
        
        downloadAllButton.setOnClickListener {
            viewModel.downloadTreatmentHistory()
        }
    }
    
    private fun setupTopTabs() {
        topTabLayout.addTab(topTabLayout.newTab().setText("TREATMENTS"))
        topTabLayout.addTab(topTabLayout.newTab().setText("PRESCRIPTIONS"))
        topTabLayout.addTab(topTabLayout.newTab().setText("REPORTS"))
        
        topTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showTreatments()
                    1 -> showPrescriptions()
                    2 -> showReports()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupHistoryTabs() {
        // This can be used for sub-categories if needed
        historyTabLayout.addTab(historyTabLayout.newTab().setText("Treatments"))
        historyTabLayout.addTab(historyTabLayout.newTab().setText("Prescriptions"))
        historyTabLayout.addTab(historyTabLayout.newTab().setText("Reports"))
        
        historyTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showTreatments()
                    1 -> showPrescriptions()
                    2 -> showReports()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupAdapters() {
        treatmentAdapter = TreatmentAdapter()
        prescriptionAdapter = PrescriptionAdapter { prescription ->
            viewModel.downloadPrescription(prescription)
        }
        reportAdapter = MedicalReportAdapter { report ->
            viewModel.downloadReport(report)
        }
    }
    
    private fun observeViewModel() {
        viewModel.treatments.observe(this) { treatments ->
            treatmentAdapter.submitList(treatments)
            if (topTabLayout.selectedTabPosition == 0) {
                contentRecyclerView.adapter = treatmentAdapter
            }
        }
        
        viewModel.prescriptions.observe(this) { prescriptions ->
            prescriptionAdapter.submitList(prescriptions)
            if (topTabLayout.selectedTabPosition == 1) {
                contentRecyclerView.adapter = prescriptionAdapter
            }
        }
        
        viewModel.reports.observe(this) { reports ->
            reportAdapter.submitList(reports)
            if (topTabLayout.selectedTabPosition == 2) {
                contentRecyclerView.adapter = reportAdapter
            }
        }
        
        viewModel.downloadStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Download failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showTreatments() {
        contentRecyclerView.adapter = treatmentAdapter
    }
    
    private fun showPrescriptions() {
        contentRecyclerView.adapter = prescriptionAdapter
    }
    
    private fun showReports() {
        contentRecyclerView.adapter = reportAdapter
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
