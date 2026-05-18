package com.johnri.labRatStudyCards.ui.cards

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import com.johnri.labRatStudyCards.ui.adapters.studyCardAdapter
import com.johnri.labRatStudyCards.ui.study.studyActivity
import com.johnri.labRatStudyCards.viewmodel.studyCardViewModel

class studyCardListActivity : AppCompatActivity() {

    private lateinit var viewModel: studyCardViewModel
    private lateinit var adapter: studyCardAdapter

    private var chapterId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studycard_list_activity)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        chapterId = intent.getIntExtra("chapter_id", -1)

        val db = appDatabase.getDatabase(this)
        val repository = studyCardRepository(db.studyCardDao())

        viewModel = studyCardViewModel(repository)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCards)

        adapter = studyCardAdapter(emptyList())

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        observeData()

        findViewById<Button>(R.id.btnStudyChapter).setOnClickListener {
            startStudy()
        }
    }

    private fun observeData() {
        viewModel.getCardsByChapter(chapterId).observe(this) { cards ->
            adapter.updateData(cards)
        }
    }

    private fun startStudy() {
        val intent = Intent(this, studyActivity::class.java)
        intent.putExtra("chapter_id", chapterId)
        startActivity(intent)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}