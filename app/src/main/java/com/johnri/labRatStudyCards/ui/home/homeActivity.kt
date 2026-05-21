package com.johnri.labRatStudyCards.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.entity.deckEntity
import com.johnri.labRatStudyCards.data.repository.deckRepository
import com.johnri.labRatStudyCards.ui.adapters.deckAdapter
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.chapters.chapterActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity
import com.johnri.labRatStudyCards.ui.quickstudy.quickStudyActivity
import kotlinx.coroutines.launch

class homeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: deckAdapter

    private val repo by lazy {
        val db = appDatabase.getDatabase(this)
        deckRepository(db.deckDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        recyclerView = findViewById(R.id.recyclerDecks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = deckAdapter(
            decks = emptyList(),
            onClick = { openDeck(it) },
            onDeleteClick = { showDeleteDialog(it) }
        )

        recyclerView.adapter = adapter

        findViewById<View>(R.id.btnCreateDeck).setOnClickListener {
            startActivity(Intent(this, importDeckActivity::class.java))
        }

        observeDecks()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_add_card -> {startActivity(Intent(this, addCardActivity::class.java))
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

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
            R.id.fabQuickStudy
        ).setOnClickListener {
            quickStudy()
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "LabRat"
    }

    private fun quickStudy() {
        val intent = Intent(this, quickStudyActivity::class.java)
        startActivity(intent)
    }

    private fun observeDecks() {

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId <= 0) {
            Toast.makeText(this, "No ha iniciado sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val emptyView = findViewById<View>(R.id.emptyState)

        lifecycleScope.launch {
            repo.getDecksByUser(userId).collect { list ->

                adapter.updateData(list)

                if (list.isEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openDeck(deck: deckEntity) {
        val intent = Intent(this, chapterActivity::class.java)
        intent.putExtra("deck_id", deck.id)
        intent.putExtra("deck_name", deck.name)
        startActivity(intent)
    }

    private fun showDeleteDialog(deck: deckEntity) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar mazo")
            .setMessage("¿Seguro que quieres eliminar este mazo?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteDeck(deck)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteDeck(deck: deckEntity) {

        lifecycleScope.launch {
            repo.deleteDeck(deck)
            Toast.makeText(this@homeActivity, "Mazo eliminado", Toast.LENGTH_SHORT).show()
        }
    }
}