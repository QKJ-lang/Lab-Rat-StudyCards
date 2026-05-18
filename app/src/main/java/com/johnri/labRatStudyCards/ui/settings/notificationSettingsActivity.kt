package com.johnri.labRatStudyCards.ui.settings

import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.notifications.notificationSchedule
import com.johnri.labRatStudyCards.ui.profile.profileActivity

class notificationSettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var btnPickTime: Button
    private lateinit var spinnerFrequency: Spinner

    private var selectedHour = 20
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_settings_activity)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Notificaciones"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        setupSpinner()
        loadPreferences()
        setupListeners()

        // Bottom nav
        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_add_card

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, homeActivity::class.java))
                    true
                }

                R.id.nav_add_card -> {
                    startActivity(Intent(this, addCardActivity::class.java))
                    true
                }

                R.id.nav_create_deck -> {
                    startActivity(Intent(this, importDeckActivity::class.java))
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, profileActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun initViews() {
        switchNotifications = findViewById(R.id.switchNotifications)
        btnPickTime = findViewById(R.id.btnPickTime)
        spinnerFrequency = findViewById(R.id.spinnerFrequency)
    }

    private fun setupSpinner() {
        val options = listOf("Diario", "Cada 2 días", "Semanal")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        spinnerFrequency.adapter = adapter
    }

    private fun loadPreferences() {
        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)

        switchNotifications.isChecked = prefs.getBoolean("notifications_enabled", false)
        selectedHour = prefs.getInt("notif_hour", 20)
        selectedMinute = prefs.getInt("notif_minute", 0)

        btnPickTime.text = formatTime(selectedHour, selectedMinute)
    }

    private fun setupListeners() {

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    requestPermissions(
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        100
                    )

                    switchNotifications.isChecked = false
                    return@setOnCheckedChangeListener
                }
            }

            savePreferences()
        }

        btnPickTime.setOnClickListener {
            showTimePicker()
        }

        spinnerFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                savePreferences()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showTimePicker() {
        val timePicker = TimePickerDialog(
            this,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                btnPickTime.text = formatTime(hour, minute)
                savePreferences()
            },
            selectedHour,
            selectedMinute,
            true
        )
        timePicker.show()
    }

    private fun savePreferences() {
        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)

        prefs.edit().apply {
            putBoolean("notifications_enabled", switchNotifications.isChecked)
            putInt("notif_hour", selectedHour)
            putInt("notif_minute", selectedMinute)
            putInt("notif_frequency", spinnerFrequency.selectedItemPosition)
            apply()
        }
        notificationSchedule.schedule(this)
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}