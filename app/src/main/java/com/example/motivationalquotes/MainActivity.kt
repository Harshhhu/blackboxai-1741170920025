package com.example.motivationalquotes

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private lateinit var quoteText: TextView
    private lateinit var notificationSwitch: SwitchMaterial
    private lateinit var categorySpinner: Spinner
    private lateinit var refreshQuoteButton: Button
    private lateinit var shareQuoteButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        quoteText = findViewById(R.id.quoteText)
        notificationSwitch = findViewById(R.id.notificationSwitch)
        categorySpinner = findViewById(R.id.categorySpinner)
        refreshQuoteButton = findViewById(R.id.refreshQuoteButton)
        shareQuoteButton = findViewById(R.id.shareQuoteButton)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MotivationalQuotes", MODE_PRIVATE)
        
        // Set up category spinner
        setupCategorySpinner()

        // Set up notification switch
        setupNotificationSwitch()

        // Set up button click listeners
        refreshQuoteButton.setOnClickListener {
            displayRandomQuote()
        }

        shareQuoteButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, quoteText.text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Quote"))
        }

        // Display a random quote
        displayRandomQuote()
    }

    private fun setupCategorySpinner() {
        // Create an ArrayAdapter using the enum values
        val categories = QuoteProvider.QuoteCategory.values().map { 
            it.name.replace('_', ' ').capitalize() 
        }.toTypedArray()

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // Set the saved category
        val savedCategory = sharedPreferences.getString("selected_category", "MOTIVATIONAL")
        val position = categories.indexOf(savedCategory?.capitalize())
        if (position != -1) {
            categorySpinner.setSelection(position)
        }

        // Listen for category changes
        categorySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedCategory = categories[position].uppercase()
                sharedPreferences.edit().putString("selected_category", selectedCategory).apply()
                displayRandomQuote()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupNotificationSwitch() {
        notificationSwitch.isChecked = sharedPreferences.getBoolean("notifications_enabled", true)
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
            if (isChecked) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayRandomQuote() {
        val categoryName = sharedPreferences.getString("selected_category", "MOTIVATIONAL")
        val category = QuoteProvider.QuoteCategory.valueOf(categoryName ?: "MOTIVATIONAL")
        val quote = QuoteProvider.getRandomQuote(category)
        quoteText.text = quote
    }

    private fun String.capitalize(): String {
        return this.lowercase().replaceFirstChar { it.uppercase() }
    }
}
