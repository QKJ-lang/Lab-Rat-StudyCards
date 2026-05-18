package com.johnri.labRatStudyCards.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.userRepository
import com.johnri.labRatStudyCards.viewmodel.authViewModel
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import com.johnri.labRatStudyCards.ui.main.MainActivity
import androidx.appcompat.widget.Toolbar

class logInActivity : AppCompatActivity() {

    private lateinit var viewModel: authViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = appDatabase.getDatabase(this)
        val repository = userRepository(db.userDao())

        viewModel = authViewModel(repository)

        viewModel.user.observe(this) { user ->
            if (user != null && user.id > 0) {
                val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
                prefs.edit().putInt("user_id", user.id).apply()

                goToMain()
            }
            else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val button = findViewById<Button>(R.id.btnLogin)

        button.setOnClickListener {

            val emailText = email.text.toString()
            val passText = password.text.toString()

            if (emailText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(emailText, passText)
        }

        findViewById<TextView>(R.id.tvSignup).setOnClickListener {
            startActivity(Intent(this, signUpActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}