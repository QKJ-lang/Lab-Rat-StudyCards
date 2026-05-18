package com.johnri.labRatStudyCards.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import com.johnri.labRatStudyCards.data.repository.userRepository
import com.johnri.labRatStudyCards.ui.auth.logInActivity
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.settings.settingsActivity
import kotlinx.coroutines.launch
import java.util.Calendar

class profileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)

        val tvToday = findViewById<TextView>(R.id.tvToday)
        val tvPending = findViewById<TextView>(R.id.tvPending)
        val tvWeek = findViewById<TextView>(R.id.tvWeek)

        val btnSettings = findViewById<Button>(R.id.btnSettings)

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        val db = appDatabase.getDatabase(this)
        val userRepo = userRepository(db.userDao())
        val cardRepo = studyCardRepository(db.studyCardDao())

        val now = System.currentTimeMillis()

        val calendar = java.util.Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val startOfWeek = calendar.timeInMillis

        lifecycleScope.launch {
            val user = userRepo.getUserById(userId)
            user?.let {
                tvName.text = it.name
                tvEmail.text = it.email
            }
        }
        lifecycleScope.launch {
            val today = cardRepo.getReviewedToday(startOfDay)
            val pending = cardRepo.getPendingToday(now)
            val week = cardRepo.getReviewedWeek(startOfWeek)

            tvToday.text = today.toString()
            tvPending.text = pending.toString()
            tvWeek.text = week.toString()
        }

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_profile

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
                    true
                }

                else -> false
            }
        }
        btnSettings.setOnClickListener {
            startActivity(Intent(this, settingsActivity::class.java))
        }

        val btnSignOut = findViewById<Button>(R.id.btnSignOut)

        btnSignOut.setOnClickListener {

            val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()
            val intent = Intent(this, logInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}