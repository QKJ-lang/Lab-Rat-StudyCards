package com.johnri.labRatStudyCards.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.ui.adapters.settingsAdapter
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity
import com.johnri.labRatStudyCards.ui.profile.profileSettingsActivity
import com.johnri.labRatStudyCards.viewmodel.settings.settingsOptions


class settingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.settingsRecyclerView)

        setupToolbar()
        setupRecycler()

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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ajustes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecycler() {
        val options = listOf(
            settingsOptions("Ajustes de perfil") {
                startActivity(Intent(this, profileSettingsActivity::class.java))
            },
            settingsOptions("Notificaciones") {
                startActivity(Intent(this, notificationSettingsActivity::class.java))
            },
            settingsOptions("Soporte") {
                startActivity(Intent(this, supportActivity::class.java))
            },
            settingsOptions("Changelog") {
                startActivity(Intent(this, changelogActivity::class.java))
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = settingsAdapter(options)
    }
}