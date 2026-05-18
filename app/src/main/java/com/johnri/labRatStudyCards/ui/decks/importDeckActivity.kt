package com.johnri.labRatStudyCards.ui.decks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.data.entity.deckEntity
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import com.johnri.labRatStudyCards.data.repository.chapterRepository
import com.johnri.labRatStudyCards.data.repository.deckRepository
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import com.johnri.labRatStudyCards.ui.home.homeActivity
import com.johnri.labRatStudyCards.ui.profile.profileActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class importDeckActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null

    private val pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedUri = it
            findViewById<TextView>(R.id.tvFileName).text = "Archivo seleccionado"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_deck_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etDeckName = findViewById<EditText>(R.id.etDeckName)
        val tvFileName = findViewById<TextView>(R.id.tvFileName)

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId == -1) return

        findViewById<Button>(R.id.btnSelectFile).setOnClickListener {
            pickFile.launch("*/*")
        }

        findViewById<Button>(R.id.btnImportCsv).setOnClickListener {

            val uri = selectedUri ?: run {
                tvFileName.text = "Selecciona un archivo primero"
                return@setOnClickListener
            }

            val name = etDeckName.text.toString()

            if (name.isEmpty()) {
                etDeckName.error = "Pon un nombre"
                return@setOnClickListener
            }

            importCsv(uri, name, userId)
        }

        findViewById<Button>(R.id.btnCreateEmptyDeck).setOnClickListener {

            val name = etDeckName.text.toString()

            if (name.isEmpty()) {
                etDeckName.error = "Pon un nombre"
                return@setOnClickListener
            }

            createEmptyDeck(name, userId)
        }

        findViewById<Button>(R.id.btnImportToExisting).setOnClickListener {

            val uri = selectedUri ?: run {
                tvFileName.text = "Selecciona un archivo primero"
                return@setOnClickListener
            }

            showDeckSelector { deckId ->
                importCsvIntoExistingDeck(uri, deckId)
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_create_deck

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> startActivity(Intent(this, homeActivity::class.java))
                R.id.nav_add_card -> startActivity(Intent(this, homeActivity::class.java))
                R.id.nav_create_deck -> {}
                R.id.nav_profile -> startActivity(Intent(this, profileActivity::class.java))
            }
            true
        }
    }

    private fun showDeckSelector(onDeckSelected: (Int) -> Unit) {

        val db = appDatabase.getDatabase(this)
        val deckRepo = deckRepository(db.deckDao())

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        lifecycleScope.launch {

            val decks = deckRepo.getDecksByUser(userId).first()

            val names = decks.map { it.name }.toTypedArray()

            runOnUiThread {
                android.app.AlertDialog.Builder(this@importDeckActivity)
                    .setTitle("Selecciona un mazo")
                    .setItems(names) { _, which ->
                        onDeckSelected(decks[which].id)
                    }
                    .show()
            }
        }
    }

    private fun importCsvIntoExistingDeck(uri: Uri, deckId: Int) {

        val db = appDatabase.getDatabase(this)
        val chapterRepo = chapterRepository(db.chapterDao())
        val cardRepo = studyCardRepository(db.studyCardDao())

        lifecycleScope.launch {

            val chapterMap = mutableMapOf<String, Int>()

            val text = contentResolver.openInputStream(uri)
                ?.bufferedReader()
                ?.readText()
                ?: return@launch

            val rows = parseCsv(text)

            val hasHeader = rows.firstOrNull()
                ?.any { it.contains("PREGUNTA", true) } == true

            val dataRows = if (hasHeader) rows.drop(1) else rows

            dataRows.forEach { parts ->

                if (parts.size < 2) return@forEach

                val question = parts[0]
                val answer = parts[1]

                val chapterName = parts.getOrNull(2)
                    ?.trim()
                    ?.lowercase()
                    ?.ifBlank { "default" }
                    ?: "default"

                val chapterId = chapterMap.getOrPut(chapterName) {
                    chapterRepo.insertChapterAndReturnId(
                        chapterEntity(name = chapterName, deckId = deckId)
                    ).toInt()
                }

                cardRepo.insertCard(
                    studyCardEntity(
                        question = question,
                        answer = answer,
                        chapterId = chapterId,
                        easiness = 2.5,
                        repetitions = 0,
                        intervals = 1,
                        nextReview = System.currentTimeMillis()
                    )
                )
            }

            Toast.makeText(this@importDeckActivity, "Añadido al mazo", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun createEmptyDeck(name: String, userId: Int) {

        val db = appDatabase.getDatabase(this)
        val deckRepo = deckRepository(db.deckDao())

        lifecycleScope.launch {
            deckRepo.insertDeck(
                deckEntity(name = name, userId = userId)
            )
            finish()
        }
    }

    private fun importCsv(uri: Uri, deckName: String, userId: Int) {

        val db = appDatabase.getDatabase(this)
        val deckRepo = deckRepository(db.deckDao())
        val chapterRepo = chapterRepository(db.chapterDao())
        val cardRepo = studyCardRepository(db.studyCardDao())

        lifecycleScope.launch {

            val deckId = deckRepo.insertDeckAndReturnId(
                deckEntity(name = deckName, userId = userId)
            ).toInt()

            val chapterMap = mutableMapOf<String, Int>()

            val text = contentResolver.openInputStream(uri)
                ?.bufferedReader()
                ?.readText()
                ?: return@launch

            val rows = parseCsv(text)

            val hasHeader = rows.firstOrNull()
                ?.any { it.contains("PREGUNTA", true) } == true

            val dataRows = if (hasHeader) rows.drop(1) else rows

            dataRows.forEach { parts ->

                if (parts.size < 2) return@forEach

                val question = parts[0]
                val answer = parts[1]

                val chapterName = parts.getOrNull(2)
                    ?.trim()
                    ?.lowercase()
                    ?: "default"

                val chapterId = chapterMap.getOrPut(chapterName) {
                    chapterRepo.insertChapterAndReturnId(
                        chapterEntity(name = chapterName, deckId = deckId)
                    ).toInt()
                }

                cardRepo.insertCard(
                    studyCardEntity(
                        question = question,
                        answer = answer,
                        chapterId = chapterId,
                        easiness = 2.5,
                        repetitions = 0,
                        intervals = 1,
                        nextReview = System.currentTimeMillis()
                    )
                )
            }

            Toast.makeText(this@importDeckActivity, "Importado correctamente", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this@importDeckActivity, homeActivity::class.java))
            finish()
        }
    }

    private fun parseCsv(text: String): List<List<String>> {

        val rows = mutableListOf<List<String>>()
        val current = StringBuilder()
        var insideQuotes = false

        for (char in text) {

            current.append(char)

            if (char == '"') insideQuotes = !insideQuotes

            if (char == '\n' && !insideQuotes) {

                val line = current.toString().trim()

                if (line.isNotBlank()) {
                    rows.add(parseCsvLine(line))
                }

                current.clear()
            }
        }

        if (current.isNotBlank()) {
            rows.add(parseCsvLine(current.toString()))
        }

        return rows
    }

    private fun parseCsvLine(line: String): List<String> {

        val result = mutableListOf<String>()
        val current = StringBuilder()
        var insideQuotes = false

        for (char in line) {

            when (char) {

                '"' -> insideQuotes = !insideQuotes

                ',' -> {
                    if (insideQuotes) {
                        current.append(char)
                    } else {
                        result.add(current.toString().trim())
                        current.clear()
                    }
                }

                else -> current.append(char)
            }
        }

        result.add(current.toString().trim())

        return result
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}