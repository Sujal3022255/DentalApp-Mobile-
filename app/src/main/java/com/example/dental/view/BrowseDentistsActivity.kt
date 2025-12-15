package com.example.dental.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.Dentist
import com.example.dental.ui.adapter.DentistAdapter
import com.example.dental.viewmodel.AppointmentViewModel
import com.google.android.material.chip.Chip

class BrowseDentistsActivity : AppCompatActivity() {
    
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var dentistsRecyclerView: RecyclerView
    private lateinit var dentistAdapter: DentistAdapter
    private lateinit var searchEditText: EditText
    private lateinit var resultsCountText: TextView
    private lateinit var filterButton: ImageButton
    
    private var allDentists = listOf<Dentist>()
    private var filteredDentists = listOf<Dentist>()
    private var selectedSpecialty = "All"
    private var minExperience = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_dentists)
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Browse Dentists"
        
        initViews()
        setupSearch()
        setupFilters()
        observeViewModel()
    }
    
    private fun initViews() {
        dentistsRecyclerView = findViewById(R.id.dentists_recycler_view)
        searchEditText = findViewById(R.id.search_edit_text)
        resultsCountText = findViewById(R.id.results_count_text)
        filterButton = findViewById(R.id.filter_button)
        
        dentistsRecyclerView.layoutManager = LinearLayoutManager(this)
        
        dentistAdapter = DentistAdapter(
            onDentistClick = { dentist -> showDentistProfile(dentist) },
            onBookClick = { dentist -> onDentistSelected(dentist) }
        )
        dentistsRecyclerView.adapter = dentistAdapter
    }
    
    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterDentists(s?.toString() ?: "")
            }
        })
    }
    
    private fun setupFilters() {
        findViewById<Chip>(R.id.chip_all).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSpecialty = "All"
                filterDentists(searchEditText.text.toString())
            }
        }
        
        findViewById<Chip>(R.id.chip_general).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSpecialty = "General Dentistry"
                filterDentists(searchEditText.text.toString())
            }
        }
        
        findViewById<Chip>(R.id.chip_orthodontics).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSpecialty = "Orthodontics"
                filterDentists(searchEditText.text.toString())
            }
        }
        
        findViewById<Chip>(R.id.chip_cosmetic).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSpecialty = "Cosmetic Dentistry"
                filterDentists(searchEditText.text.toString())
            }
        }
        
        findViewById<Chip>(R.id.chip_pediatric).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSpecialty = "Pediatric Dentistry"
                filterDentists(searchEditText.text.toString())
            }
        }
        
        filterButton.setOnClickListener {
            showExperienceFilter()
        }
    }
    
    private fun showExperienceFilter() {
        val options = arrayOf("All Experience", "5+ years", "10+ years", "15+ years")
        val values = arrayOf(0, 5, 10, 15)
        
        AlertDialog.Builder(this)
            .setTitle("Filter by Experience")
            .setItems(options) { _, which ->
                minExperience = values[which]
                filterDentists(searchEditText.text.toString())
            }
            .show()
    }
    
    private fun filterDentists(query: String) {
        filteredDentists = allDentists.filter { dentist ->
            val matchesSearch = query.isEmpty() || 
                dentist.name.contains(query, ignoreCase = true) ||
                dentist.specialty.contains(query, ignoreCase = true)
            
            val matchesSpecialty = selectedSpecialty == "All" || 
                dentist.specialty.contains(selectedSpecialty, ignoreCase = true)
            
            val matchesExperience = dentist.experience >= minExperience
            
            matchesSearch && matchesSpecialty && matchesExperience
        }
        
        dentistAdapter.submitList(filteredDentists)
        updateResultsCount()
    }
    
    private fun updateResultsCount() {
        val count = filteredDentists.size
        resultsCountText.text = "$count dentist${if (count != 1) "s" else ""} found"
    }
    
    private fun showDentistProfile(dentist: Dentist) {
        val intent = Intent(this, DentistProfileActivity::class.java)
        intent.putExtra("DENTIST", dentist)
        startActivity(intent)
    }
    
    private fun observeViewModel() {
        viewModel.dentists.observe(this) { dentistList ->
            allDentists = dentistList
            filteredDentists = dentistList
            dentistAdapter.submitList(filteredDentists)
            updateResultsCount()
        }
    }
    
    private fun onDentistSelected(dentist: Dentist) {
        val intent = Intent(this, BookAppointmentActivity::class.java)
        intent.putExtra("DENTIST", dentist)
        startActivity(intent)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
