package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiating UI elements
        val register = findViewById<Button>(R.id.registerButtonRegister)
        val login = findViewById<TextView>(R.id.loginTextViewRegister)

        register.setOnClickListener {
            performRegister()
        }

        login.setOnClickListener {
            Log.d("MainActivity", "Login pressed!")

            // Go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        // Instantiating UI elements
        val email = findViewById<EditText>(R.id.emailEditTextRegister)
        val password = findViewById<EditText>(R.id.passwordEditTextRegister)

        if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email: ${email.text.toString()}")
        Log.d("MainActivity", "Password: ${password.text.toString()}")

        // Firebase Authentication
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("MainActivity", "Registration successful! UID: ${user!!.uid}")
                }
                else {
                    Log.w("MainActivity", "Registration failed", task.exception)
                    Toast.makeText(this, "Registration failed! ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}