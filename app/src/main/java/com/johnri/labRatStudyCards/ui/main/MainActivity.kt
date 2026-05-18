package com.johnri.labRatStudyCards.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity
import com.johnri.labRatStudyCards.ui.quickstudy.quickStudyActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var floatingBtn: FloatingActionButton

    private fun loadActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        bottomNav = findViewById(R.id.bottomNav)
        floatingBtn = findViewById(R.id.fabStudy)

        floatingBtn.setOnClickListener {
            loadActivity(quickStudyActivity::class.java)
        }


// Pantalla inicial
        loadActivity(homeActivity::class.java)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> loadActivity(homeActivity::class.java)

                R.id.nav_add_card -> loadActivity(addCardActivity::class.java)

                R.id.nav_create_deck -> loadActivity(importDeckActivity::class.java)

                R.id.nav_profile -> loadActivity(profileActivity::class.java)
            }
            true
        }
    }
}