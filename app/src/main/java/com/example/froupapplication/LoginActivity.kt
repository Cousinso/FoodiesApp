package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
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

                        val intent = Intent(this, LatestMessagesActivity::class.java)
                        // Clears intent list and back button goes to home screen
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
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