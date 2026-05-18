package com.johnri.labRatStudyCards.ui.intro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.ui.adapters.introAdapter
import com.johnri.labRatStudyCards.ui.auth.signUpActivity
import com.johnri.labRatStudyCards.ui.main.MainActivity
import kotlinx.coroutines.launch
import com.johnri.labRatStudyCards.data.repository.userRepository


class introActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private lateinit var progressBar: ProgressBar
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)
        val db = appDatabase.getDatabase(this)
        val userRepo = userRepository(db.userDao())

        lifecycleScope.launch {

            if (userId != -1) {

                val user = userRepo.getUserById(userId)

                if (user != null) {
                    startActivity(Intent(this@introActivity, MainActivity::class.java))
                    finish()
                } else {
                    prefs.edit().clear().apply()
                }
            }

            setContentView(R.layout.intro_activity)

            viewPager = findViewById(R.id.viewPager)
            btnNext = findViewById(R.id.btnNext)

            val slides = listOf(
                introSlide("¿Sabías que las StudyCards se usaron por primera vez en 1834 por Favell Lee para ayudar al estudio?", R.drawable.ic_favell_lee_intro),
                introSlide("Su visión era simplificar el aprendizaje y fortalecer la memoria con la simpleza de las Flash Cards.", R.drawable.ic_brain_intro),
                introSlide("Actualmente, su forma de estudio es la más alabada tanto por estudiantes como profesores alrededor del mundo.", R.drawable.ic_worlwide_intro),
                introSlide("Lab Rat tiene como objetivo seguir con las Study Cards, llevándola a tu bolsillo. ¿Listo para empezar?", R.drawable.ic_study_rat)
            )

            viewPager.adapter = introAdapter(slides)

            btnNext.setOnClickListener {

                if (viewPager.currentItem < slides.size - 1) {
                    viewPager.currentItem += 1
                } else {
                    prefs.edit().putBoolean("first_time", false).apply()

                    startActivity(Intent(this@introActivity, signUpActivity::class.java))
                    finish()
                }
            }

            progressBar = findViewById(R.id.progressBar)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    progressBar.progress = position + 1
                }
            })
        }
    }
}