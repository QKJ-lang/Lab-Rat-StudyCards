package com.johnri.labRatStudyCards.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity

class supportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_activity)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.title = "Soporte"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvEmail = findViewById<TextView>(R.id.tvEmail)

        tvEmail.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:soporte@labratcartasestudio.xyz")
                putExtra(Intent.EXTRA_SUBJECT, "Soporte LabRat")
            }

            startActivity(intent)
        }

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}