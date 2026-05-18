package com.johnri.labRatStudyCards.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.deckRepository
import com.johnri.labRatStudyCards.data.repository.userRepository
import com.johnri.labRatStudyCards.ui.cards.addCardActivity
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import kotlinx.coroutines.launch

class profileSettingsActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDeleteAllDecks: Button

    private lateinit var userRepo: userRepository
    private lateinit var deckRepo: deckRepository

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_settings_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Perfil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        initData()
        loadUser()
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
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)
        btnDeleteAllDecks = findViewById(R.id.btnDeleteAllDecks)
    }

    private fun initData() {
        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        userId = prefs.getInt("user_id", -1)

        val db = appDatabase.getDatabase(this)
        userRepo = userRepository(db.userDao())
        deckRepo = deckRepository(db.deckDao())
    }

    private fun loadUser() {
        lifecycleScope.launch {
            val user = userRepo.getUserById(userId)
            user?.let {
                etName.setText(it.name)
                etEmail.setText(it.email)
            }
        }
    }

    private fun setupListeners() {

        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            val newEmail = etEmail.text.toString()
            val newPassword = etPassword.text.toString()

            lifecycleScope.launch {
                val user = userRepo.getUserById(userId)

                user?.let {
                    val updatedUser = it.copy(
                        name = newName,
                        email = newEmail,
                        password = if (newPassword.isNotEmpty()) newPassword else it.password
                    )

                    userRepo.updateUser(updatedUser)

                    Toast.makeText(this@profileSettingsActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDeleteAllDecks.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar todos los mazos")
            .setMessage("Esta acción no se puede deshacer. ¿Seguro?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteAllDecks()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAllDecks() {
        lifecycleScope.launch {
            deckRepo.deleteAllDecksByUser(userId)
            Toast.makeText(this@profileSettingsActivity, "Mazos eliminados", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}