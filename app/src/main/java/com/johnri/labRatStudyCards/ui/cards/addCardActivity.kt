package com.johnri.labRatStudyCards.ui.cards

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import com.johnri.labRatStudyCards.ui.decks.importDeckActivity
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity
import com.johnri.labRatStudyCards.viewmodel.addCardViewModel.addCardViewModelFactory
import com.johnri.labRatStudyCards.viewmodel.addCardViewModel.addCardViewModel
import jp.wasabeef.richeditor.RichEditor
import androidx.lifecycle.lifecycleScope
import com.johnri.labRatStudyCards.data.repository.chapterRepository
import kotlinx.coroutines.launch

class addCardActivity : AppCompatActivity() {

    private lateinit var viewModel: addCardViewModel
    private var selectedChapterId: Int? = null
    private lateinit var editorQuestion: RichEditor
    private lateinit var editorAnswer: RichEditor

    private var activeEditor: RichEditor? = null

    private var deckId: Int = -1

    private lateinit var repository: studyCardRepository
    private lateinit var chapterRepository: chapterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_activity)

        deckId = intent.getIntExtra("deck_id", -1)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = appDatabase.getDatabase(this)
        val dao = db.studyCardDao()
        val chapterDao = db.chapterDao()
        chapterRepository = chapterRepository(chapterDao)
        repository = studyCardRepository(dao)

        viewModel = ViewModelProvider(
            this,
            addCardViewModelFactory(repository)
        )[addCardViewModel::class.java]

        // Editor
        editorQuestion = findViewById(R.id.editorQuestion)
        editorAnswer = findViewById(R.id.editorAnswer)
        editorQuestion.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) activeEditor = editorQuestion
        }

        editorAnswer.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) activeEditor = editorAnswer
        }
        activeEditor = editorQuestion


        // ChapterId
        selectedChapterId = intent.getIntExtra("chapterId", -1)
        if (selectedChapterId == -1) selectedChapterId = null

        // Botones formato
        findViewById<Button>(R.id.btnBold).setOnClickListener {
            activeEditor?.setBold()
        }

        findViewById<Button>(R.id.btnItalic).setOnClickListener {
            activeEditor?.setItalic()
        }

        findViewById<Button>(R.id.btnUnderline).setOnClickListener {
            activeEditor?.setUnderline()
        }

        findViewById<Button>(R.id.btnBullet).setOnClickListener {
            activeEditor?.setBullets()
        }

        // Guardar
        findViewById<Button>(R.id.btnSave).setOnClickListener {

            val questionHtml = editorQuestion.html?.trim() ?: ""
            val answerHtml = editorAnswer.html?.trim() ?: ""

            if (questionHtml.isBlank() ||
                questionHtml == "<br>" ||
                questionHtml == "<div><br></div>"
            ) {
                Toast.makeText(this, "Pregunta vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (answerHtml.isBlank() ||
                answerHtml == "<br>" ||
                answerHtml == "<div><br></div>"
            ) {
                Toast.makeText(this, "Respuesta vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedChapterId == null) {
                loadChaptersAndShowDialog()
                return@setOnClickListener
            }

            saveCard()
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

                R.id.nav_add_card -> true

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

    private fun saveCard() {

        val questionHtml = editorQuestion.html?.trim() ?: ""
        val answerHtml = editorAnswer.html?.trim() ?: ""

        if (questionHtml.isBlank()) {
            Toast.makeText(this, "Pregunta vacía", Toast.LENGTH_SHORT).show()
            return
        }

        if (answerHtml.isBlank()) {
            Toast.makeText(this, "Respuesta vacía", Toast.LENGTH_SHORT).show()
            return
        }

        val chapterId = selectedChapterId ?: return

        viewModel.addCard(
            questionHtml = questionHtml,
            answerHtml = answerHtml,
            chapterId = chapterId
        )

        Toast.makeText(this, "Tarjeta guardada", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun loadChaptersAndShowDialog() {

        lifecycleScope.launch {

            chapterRepository.getAllChapters().collect { chapters ->

                val names = chapters.map { it.name }.toTypedArray()

                AlertDialog.Builder(this@addCardActivity)
                    .setTitle("Selecciona capítulo")
                    .setItems(names) { _, which ->

                        selectedChapterId = chapters[which].id
                        saveCard()
                    }
                    .show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}