package com.johnri.labRatStudyCards.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.data.repository.userRepository
import com.johnri.labRatStudyCards.viewmodel.authViewModel
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.johnri.labRatStudyCards.ui.home.homeActivity


class signUpActivity : AppCompatActivity() {

    private lateinit var viewModel: authViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = appDatabase.getDatabase(this)
        val repository = userRepository(db.userDao())

        viewModel = authViewModel(repository)

        viewModel.user.observe(this) { user ->
            if (user != null && user.id > 0) {

                val prefs = getSharedPreferences("labrat_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putInt("user_id", user.id)
                    .apply()

                goToMain()
            } else {
                Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }

        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val button = findViewById<Button>(R.id.btnSignUp)

        button.setOnClickListener {

            val nameText = name.text.toString()
            val emailText = email.text.toString()
            val passText = password.text.toString()

            if (nameText.isEmpty() || emailText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(nameText, emailText, passText)
        }

        findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            startActivity(Intent(this, logInActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun goToMain() {
        startActivity(Intent(this, homeActivity::class.java))
        finish()
    }
}