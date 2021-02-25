package com.example.froupapplication

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Instantiating UI elements
        val register = findViewById<Button>(R.id.registerButtonRegister)
        val login = findViewById<TextView>(R.id.loginTextViewRegister)
        val photo = findViewById<Button>(R.id.photoButtonRegister)

        register.setOnClickListener {
            performRegister()
        }

        login.setOnClickListener {
            Log.d("RegisterActivity", "Login pressed!")

            // Go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        photo.setOnClickListener {
            Log.d("RegisterActivity", "Select photo pressed!")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    // Called when the select photo button is pressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // Check selected image
            Log.d("RegisterActivity", "Photo was selected")

            // Gives location of image data
            selectedPhotoUri = data.data

            // CircleImageView from GitHub
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val imageView = findViewById<CircleImageView>(R.id.photoImageViewRegister)
            imageView.setImageBitmap(bitmap)
            val photoButton = findViewById<Button>(R.id.photoButtonRegister)
            photoButton.alpha = 0f
        }
    }

    // Called when the register button is pressed
    private fun performRegister() {
        // Instantiating UI elements
        val email = findViewById<EditText>(R.id.emailEditTextRegister)
        val password = findViewById<EditText>(R.id.passwordEditTextRegister)

        if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email: ${email.text.toString()}")
        Log.d("RegisterActivity", "Password: ${password.text.toString()}")

        // Firebase Authentication
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    photoUploadToFirebase()
                    Log.d("RegisterActivity", "Registration successful! UID: ${user!!.uid}")
                }
                else {
                    Log.w("RegisterActivity", "Registration failed", task.exception)
                    Toast.makeText(this, "Registration failed! ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Called when function for register button is pressed to upload photo to Firebase Storage
    private fun photoUploadToFirebase() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance()
        val ref = storage.getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Image uploaded! Image path: ${it.metadata?.path}")

                ref.downloadUrl.addOnCompleteListener {
                    Log.d("RegisterActivity", "File location: $it")

                    saveUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                // Blank for the time being
            }
    }

    // Called in photo upload function to save user to Firebase Database
    private fun saveUserToDatabase(profileImageUrl: String) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("/users/${auth.uid}")
        val username = findViewById<EditText>(R.id.usernameEditTextRegister)

        val user = User(auth.uid ?: "", username.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User saved to Firebase Database")
            }
            .addOnFailureListener {
                // Blank for the time being
            }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)