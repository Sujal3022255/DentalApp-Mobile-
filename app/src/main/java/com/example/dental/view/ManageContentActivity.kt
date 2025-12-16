package com.example.dental.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dental.R
import com.example.dental.data.model.DentalTip
import com.example.dental.viewmodel.AdminViewModel
import com.google.firebase.auth.FirebaseAuth

class ManageContentActivity : AppCompatActivity() {
    
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnAddContent: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_content)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Manage Content"
        
        initViews()
        observeViewModel()
        setupClickListeners()
        
        viewModel.loadDentalTips()
    }
    
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewContent)
        progressBar = findViewById(R.id.progressBar)
        btnAddContent = findViewById(R.id.btnAddContent)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.dentalTips.observe(this) { tips ->
            // Set up content adapter with edit/delete actions
            Toast.makeText(this, "Loaded ${tips.size} tips", Toast.LENGTH_SHORT).show()
        }
        
        viewModel.operationStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        btnAddContent.setOnClickListener {
            showAddContentDialog()
        }
    }
    
    private fun showAddContentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_content, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etContentTitle)
        val etContent = dialogView.findViewById<EditText>(R.id.etContentText)
        val etCategory = dialogView.findViewById<EditText>(R.id.etContentCategory)
        
        AlertDialog.Builder(this)
            .setTitle("Add Dental Tip")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val tip = DentalTip(
                    title = etTitle.text.toString(),
                    content = etContent.text.toString(),
                    category = etCategory.text.toString(),
                    createdBy = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    createdAt = System.currentTimeMillis()
                )
                viewModel.addDentalTip(tip)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    fun editContent(tip: DentalTip) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_content, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etContentTitle)
        val etContent = dialogView.findViewById<EditText>(R.id.etContentText)
        val etCategory = dialogView.findViewById<EditText>(R.id.etContentCategory)
        
        etTitle.setText(tip.title)
        etContent.setText(tip.content)
        etCategory.setText(tip.category)
        
        AlertDialog.Builder(this)
            .setTitle("Edit Dental Tip")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedTip = tip.copy(
                    title = etTitle.text.toString(),
                    content = etContent.text.toString(),
                    category = etCategory.text.toString()
                )
                viewModel.updateDentalTip(updatedTip)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    fun deleteContent(tipId: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Content")
            .setMessage("Are you sure you want to delete this dental tip?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteDentalTip(tipId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
