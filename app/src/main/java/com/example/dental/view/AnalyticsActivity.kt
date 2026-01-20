package com.example.dental.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dental.R
import com.example.dental.viewmodel.AdminViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

class AnalyticsActivity : AppCompatActivity() {
    
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var tvWeeklyBookings: TextView
    private lateinit var tvMonthlyBookings: TextView
    private lateinit var pieChartStatus: PieChart
    private lateinit var barChartBookings: BarChart
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        
        supportActionBar?.hide()
        
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
        
        initViews()
        observeViewModel()
        
        viewModel.loadDashboardStats()
    }
    
    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        tvWeeklyBookings = findViewById(R.id.tvWeeklyBookings)
        tvMonthlyBookings = findViewById(R.id.tvMonthlyBookings)
        pieChartStatus = findViewById(R.id.pieChartStatus)
        barChartBookings = findViewById(R.id.barChartBookings)
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.appointmentStats.observe(this) { stats ->
            tvWeeklyBookings.text = "Weekly: ${stats.weeklyAppointments}"
            tvMonthlyBookings.text = "Monthly: ${stats.monthlyAppointments}"
            
            // Setup Pie Chart for appointment status
            setupPieChart(stats)
            
            // Setup Bar Chart for bookings
            setupBarChart(stats)
        }
        
        viewModel.operationStatus.observe(this) { result ->
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupPieChart(stats: com.example.dental.data.model.AppointmentStats) {
        val entries = mutableListOf<PieEntry>()
        
        // Check if there's any data
        val hasData = stats.pendingAppointments > 0 || stats.approvedAppointments > 0 || 
                      stats.completedAppointments > 0 || stats.cancelledAppointments > 0
        
        if (hasData) {
            // Use real data
            if (stats.pendingAppointments > 0) {
                entries.add(PieEntry(stats.pendingAppointments.toFloat(), "Pending"))
            }
            if (stats.approvedAppointments > 0) {
                entries.add(PieEntry(stats.approvedAppointments.toFloat(), "Approved"))
            }
            if (stats.completedAppointments > 0) {
                entries.add(PieEntry(stats.completedAppointments.toFloat(), "Completed"))
            }
            if (stats.cancelledAppointments > 0) {
                entries.add(PieEntry(stats.cancelledAppointments.toFloat(), "Cancelled"))
            }
        } else {
            // Show sample data when no appointments exist
            entries.add(PieEntry(5f, "Pending"))
            entries.add(PieEntry(8f, "Approved"))
            entries.add(PieEntry(12f, "Completed"))
            entries.add(PieEntry(3f, "Cancelled"))
        }
        
        val dataSet = PieDataSet(entries, "Appointment Status")
        dataSet.colors = listOf(
            Color.parseColor("#FFA726"), // Orange for pending
            Color.parseColor("#42A5F5"), // Blue for approved
            Color.parseColor("#66BB6A"), // Green for completed
            Color.parseColor("#EF5350")  // Red for cancelled
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        
        val data = PieData(dataSet)
        pieChartStatus.data = data
        pieChartStatus.description.isEnabled = false
        pieChartStatus.setEntryLabelColor(Color.BLACK)
        pieChartStatus.setEntryLabelTextSize(10f)
        pieChartStatus.animateY(1000)
        pieChartStatus.invalidate()
    }
    
    private fun setupBarChart(stats: com.example.dental.data.model.AppointmentStats) {
        val entries = mutableListOf<BarEntry>()
        
        // Check if there's any data
        val hasData = stats.todayAppointments > 0 || stats.weeklyAppointments > 0 || stats.monthlyAppointments > 0
        
        if (hasData) {
            // Use real data
            entries.add(BarEntry(0f, stats.todayAppointments.toFloat()))
            entries.add(BarEntry(1f, stats.weeklyAppointments.toFloat()))
            entries.add(BarEntry(2f, stats.monthlyAppointments.toFloat()))
        } else {
            // Show sample data when no appointments exist
            entries.add(BarEntry(0f, 3f))  // Today
            entries.add(BarEntry(1f, 15f)) // Weekly
            entries.add(BarEntry(2f, 45f)) // Monthly
        }
        
        val dataSet = BarDataSet(entries, "Bookings")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        
        val data = BarData(dataSet)
        barChartBookings.data = data
        barChartBookings.description.isEnabled = false
        barChartBookings.xAxis.setDrawLabels(true)
        barChartBookings.xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    0 -> "Today"
                    1 -> "Weekly"
                    2 -> "Monthly"
                    else -> ""
                }
            }
        }
        barChartBookings.animateY(1000)
        barChartBookings.invalidate()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
