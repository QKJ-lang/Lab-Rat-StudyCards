package com.johnri.labRatStudyCards.ui.debug

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.deckRepository
import kotlinx.coroutines.launch

class debugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debug_activity)

        val tv = findViewById<TextView>(R.id.tvDebug)

        val db = appDatabase.Companion.getDatabase(this)
        val repo = deckRepository(db.deckDao())

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        lifecycleScope.launch {

            val decks = repo.getDecksByUser(userId)

            decks.collect { list ->

                val text = buildString {

                    append("USER ID ACTUAL: $userId\n\n")

                    append("DECKS EN BD:\n\n")

                    list.forEach {
                        append("ID: ${it.id}\n")
                        append("NAME: ${it.name}\n")
                        append("USER_ID: ${it.userId}\n")
                        append("----------------------\n")
                    }

                    append("\nTOTAL: ${list.size}")
                }

                tv.text = text
            }
        }
    }
}