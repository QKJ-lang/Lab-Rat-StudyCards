package com.johnri.labRatStudyCards.ui.chapters

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.data.repository.chapterRepository
import com.johnri.labRatStudyCards.data.repository.deckRepository
import com.johnri.labRatStudyCards.ui.adapters.chapterAdapter
import com.johnri.labRatStudyCards.ui.cards.studyCardListActivity
import com.johnri.labRatStudyCards.ui.quickstudy.quickStudyActivity
import com.johnri.labRatStudyCards.ui.study.studyActivity
import com.johnri.labRatStudyCards.viewmodel.chapter.chapterViewModel
import com.johnri.labRatStudyCards.viewmodel.chapter.chapterViewModelFactory
import com.johnri.labRatStudyCards.viewmodel.deckViewModel.deckViewModel
import com.johnri.labRatStudyCards.viewmodel.deckViewModel.deckViewModelFactory

class chapterActivity : AppCompatActivity() {

    private lateinit var adapter: chapterAdapter
    private var deckId: Int = -1
    lateinit var tvDeckTitle: TextView
    private lateinit var btnQuickStudy: FloatingActionButton
    private var deckName: String = ""
    private val chapterViewModel: chapterViewModel by viewModels {
        val db = appDatabase.getDatabase(this)
        val repo = chapterRepository(db.chapterDao())
        chapterViewModelFactory(repo)
    }

    private val deckViewModel: deckViewModel by viewModels {
        val db = appDatabase.getDatabase(this)
        val repo = deckRepository(db.deckDao())
        deckViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.chapter_activity)

        tvDeckTitle = findViewById(R.id.tvDeckTitle)
        btnQuickStudy = findViewById(R.id.btnQuickStudy)

        // Datos del Deck
        deckId = intent.getIntExtra("deck_id", -1)
        deckName = intent.getStringExtra("deck_name") ?: "Sin nombre"

        tvDeckTitle.text = deckName

        super.onCreate(savedInstanceState)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecycler()
        setupButtons()
        observeData()

        btnQuickStudy.setOnClickListener {

            val intent = Intent(this, quickStudyActivity::class.java)
            intent.putExtra("deck_id", deckId)
            intent.putExtra("deck_name", deckName)
            startActivity(intent)
        }
    }

    private fun setupRecycler() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerChapters)

        adapter = chapterAdapter(

            emptyList(),

            onClick = { chapter ->
                openChapterCards(chapter.id)
            },

            onLongClick = { chapter ->
                showDeleteChapterDialog(chapter)
            }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnStudyDeck).setOnClickListener {
            studyWholeDeck()
        }

        findViewById<Button>(R.id.btnCreateChapter).setOnClickListener {
            showCreateChapterDialog()
        }

        findViewById<ImageButton>(R.id.btnEditDeck).setOnClickListener {
            showEditDeckDialog()
        }
    }

    private fun observeData() {
        chapterViewModel.getChapters(deckId).observe(this) {
            adapter.updateData(it)
        }
    }

    private fun showCreateChapterDialog() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("Crear capítulo")
            .setView(editText)
            .setPositiveButton("Crear") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotBlank()) {
                    chapterViewModel.createChapter(deckId, name)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditDeckDialog() {

        val editText = EditText(this)

        editText.hint = "Nuevo título del deck"

        AlertDialog.Builder(this)
            .setTitle("Editar deck")
            .setView(editText)

            .setPositiveButton("Guardar") { _, _ ->

                val newTitle = editText.text.toString()

                if (newTitle.isNotBlank()) {

                    deckViewModel.updateDeck(
                        deckId,
                        newTitle,
                        "#FF5722"
                    )
                }
            }

            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteChapterDialog(chapter: chapterEntity) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar capítulo")
            .setMessage(
                "¿Seguro que quieres eliminar '${chapter.name}'?"
            )

            .setPositiveButton("Eliminar") { _, _ ->

                chapterViewModel.deleteChapter(chapter)
            }

            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun studyWholeDeck() {
        startActivity(Intent(this, studyActivity::class.java).apply {
            putExtra("deck_id", deckId)
        })
    }

    private fun openChapterCards(chapterId: Int) {
        startActivity(Intent(this, studyCardListActivity::class.java).apply {
            putExtra("chapter_id", chapterId)
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}