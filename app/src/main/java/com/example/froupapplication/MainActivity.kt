package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiating UI elements
        val email = findViewById<EditText>(R.id.emailEditTextRegister)
        val password = findViewById<EditText>(R.id.passwordEditTextRegister)
        val register = findViewById<Button>(R.id.registerButtonRegister)
        val login = findViewById<TextView>(R.id.loginTextViewRegister)

        register.setOnClickListener {
            Log.d("MainActivity", "Email: ${email.text.toString()}")
            Log.d("MainActivity", "Password: ${password.text.toString()}")
        }

        login.setOnClickListener {
            Log.d("MainActivity", "Login pressed!")

            // Go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}