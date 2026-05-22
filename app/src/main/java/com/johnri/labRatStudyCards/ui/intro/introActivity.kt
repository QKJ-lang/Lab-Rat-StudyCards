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

        setContentView(R.layout.intro_activity)

        val fromHelp = intent.getBooleanExtra("from_help", false)

        val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        val db = appDatabase.getDatabase(this)
        val userRepo = userRepository(db.userDao())

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        progressBar = findViewById(R.id.progressBar)

        lifecycleScope.launch {

            // Abrir desde support
            if (!fromHelp) {

                if (userId != -1) {

                    val user = userRepo.getUserById(userId)

                    if (user != null) {
                        startActivity(Intent(this@introActivity, MainActivity::class.java))
                        finish()
                        return@launch
                    } else {
                        prefs.edit().clear().apply()
                    }
                }
            }

            val slides = listOf(
                introSlide(
                    "¿Sabías que las StudyCards se usaron por primera vez en 1834 por Favell Lee para ayudar al estudio?",
                    R.drawable.ic_favell_lee_intro
                ),
                introSlide(
                    "Su visión era simplificar el aprendizaje y fortalecer la memoria con la simpleza de las Flash Cards.",
                    R.drawable.ic_brain_intro
                ),
                introSlide(
                    "Actualmente, su forma de estudio es la más alabada tanto por estudiantes como profesores alrededor del mundo.",
                    R.drawable.ic_worlwide_intro
                ),
                introSlide(
                    "Lab Rat tiene como objetivo seguir con la simplicidad y efectividad de las StudyCards, llevándola a tu bolsillo.",
                    R.drawable.ic_study_rat
                ),
                introSlide(
                    "En esta aplicación podrás crear tus propias StudyCards e importarlas gracias a la importación de mazos.",
                    R.drawable.ic_csv_rat
                ),
                introSlide(
                    "Las StudyCards tienen una cara de pregunta y otra de respuesta al girar. Al hacer click, tendrás varias botones para determinar tus conocimientos:",
                    R.drawable.ic_studycards_intro
                ),
                introSlide(
                    "✘,✔, Muy Difícil y Muy Fácil. Estas opciones ayudan al algoritmo de Repetición Espaciada para optimizar la memorización. Cuanto más fácil, menos se enseña, y viceversa.",
                    R.drawable.ic_robot_intro
                ),
                introSlide(
                    "Si tienes alguna duda o quieres volver a ver esta introducción, tienes más información en la pantalla de perfil, 'Ajustes', 'Soporte', y 'Ver Introducción'. ¿Listo para Empezar?",
                    R.drawable.ic_support_intro
                )
            )

            viewPager.adapter = introAdapter(slides)

            btnNext.setOnClickListener {

                if (viewPager.currentItem < slides.size - 1) {

                    viewPager.currentItem += 1

                } else {

                    if (fromHelp) {

                        finish()

                    } else {

                        prefs.edit().putBoolean("first_time", false).apply()

                        startActivity(
                            Intent(
                                this@introActivity,
                                signUpActivity::class.java
                            )
                        )

                        finish()
                    }
                }
            }

            viewPager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                        progressBar.progress = position + 1
                    }
                }
            )
        }
    }
}