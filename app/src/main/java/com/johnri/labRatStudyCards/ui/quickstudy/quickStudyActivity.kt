package com.johnri.labRatStudyCards.ui.quickstudy

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import com.johnri.labRatStudyCards.viewmodel.studyViewModel

class quickStudyActivity : AppCompatActivity() {

    private lateinit var viewModel: studyViewModel

    private lateinit var tvCard: TextView
    private lateinit var buttonsLayout: View
    private lateinit var imgTwist: ImageView

    private var showingAnswer = false
    private var currentCard: studyCardEntity? = null
    private var againCount = 0
    private var wrongCount = 0
    private var goodCount = 0
    private var easyCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvCard = findViewById(R.id.tvCard)
        buttonsLayout = findViewById(R.id.layoutButtons)
        imgTwist = findViewById(R.id.imgTwist)

        val db = appDatabase.getDatabase(this)
        val repository = studyCardRepository(db.studyCardDao())
        viewModel = studyViewModel(repository)

        viewModel.loadRandomCards(30)

        setupClicks()
        observeCards()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeCards() {
        viewModel.cards.observe(this) { cards ->

            if (cards.isEmpty()) {
                showResults()
                return@observe
            }
            val card = cards[0]
            currentCard = card
            showingAnswer = false

            tvCard.text = card.question
            buttonsLayout.visibility = View.GONE
        }
    }

    private fun setupClicks() {

        tvCard.setOnClickListener {

            val card = currentCard ?: return@setOnClickListener

            showingAnswer = !showingAnswer
            buttonsLayout.visibility = if (showingAnswer) View.VISIBLE else View.INVISIBLE


            tvCard.animate()
                .rotationY(90f)
                .setDuration(70)

            imgTwist.animate()
                .rotationY(90f)
                .setDuration(70)
                .withEndAction {

                    val html = if (showingAnswer) card.answer else card.question

                    tvCard.text = HtmlCompat.fromHtml(
                        html,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    tvCard.rotationY = -90f
                    imgTwist.rotationY = -90f

                    tvCard.animate()
                        .rotationY(0f)
                        .setDuration(70)
                        .start()
                    imgTwist.animate()
                        .rotationY(0f)
                        .setDuration(70)
                        .start()
                }
                .start()
        }

        findViewById<View>(R.id.btnAgain).setOnClickListener { answer(0) }
        findViewById<View>(R.id.btnWrong).setOnClickListener { answer(1) }
        findViewById<View>(R.id.btnGood).setOnClickListener { answer(2) }
        findViewById<View>(R.id.btnEasy).setOnClickListener { answer(3) }
    }

    private fun answer(quality: Int) {
        when (quality) {
            0 -> againCount++
            1 -> wrongCount++
            2 -> goodCount++
            3 -> easyCount++
        }
        viewModel.answerCard(quality)
    }

    private fun showResults() {

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Resultado")
            .setMessage(
                "Otra vez: $againCount\n" +
                        "Mal: $wrongCount\n" +
                        "Bien: $goodCount\n" +
                        "Fácil: $easyCount"
            )
            .setPositiveButton("OK") { _, _ -> finish() }
            .show()
    }
}