package com.example.froupapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Instantiating UI components
        val login = findViewById<Button>(R.id.loginButtonLogin)
        val register = findViewById<TextView>(R.id.registerTextViewLogin)

        login.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditTextLogin)
            val password = findViewById<EditText>(R.id.passwordEditTextLogin)

            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("LoginActivity", "Login pressed!")

            // Firebase Authentication
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d("LoginActivity", "Login Successful! UID: ${user!!.uid}")
                    }
                    else {
                        Log.w("MainActivity", "Registration failed", task.exception)
                        Toast.makeText(this, "Registration failed! ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Go to main activity
        register.setOnClickListener {
            finish()
        }
    }
}