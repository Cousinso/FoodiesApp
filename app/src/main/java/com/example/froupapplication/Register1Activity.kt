package com.example.froupapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
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
import kotlinx.android.parcel.Parcelize
import java.util.*

class Register1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        // Instantiating UI elements
        val register = findViewById<Button>(R.id.ButtonFinish)
        val login = findViewById<TextView>(R.id.loginTextViewRegister)
        val photo = findViewById<Button>(R.id.photoButtonRegister)

        register.setOnClickListener {
            Log.d("register1", "Register pressed!")
            performRegister()
        }
        //this is comment
        login.setOnClickListener {
            Log.d("register1", "Login pressed!")

            // Go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        photo.setOnClickListener {
            Log.d("register1", "Select photo pressed!")

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
            Log.d("register1", "Photo was selected")

            // Gives location of image data
            selectedPhotoUri = data.data

            // https://github.com/hdodenhof/CircleImageView
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
        val passwordCheck = findViewById<EditText>(R.id.passwordConfirm)

        if (email.text.toString().isEmpty() ) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.text.toString() != passwordCheck.text.toString()) {
            Toast.makeText(this, "The two passwords are not equal, please try again", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPhotoUri == null) {
            Toast.makeText(this, "Please choose a picture", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("register1", "Email: ${email.text.toString()}")
        Log.d("register1", "Password: ${password.text.toString()}")

        // Firebase Authentication
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    photoUploadToFirebase()
                    Log.d("register1", "Registration successful! UID: ${user!!.uid}")

                }
                else {
                    Log.w("register1", "Registration failed", task.exception)
                    Toast.makeText(this, "Registration failed! ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Called when function for register button is pressed to upload photo to Firebase Storage
    private fun photoUploadToFirebase() {

        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance()
        val ref = storage.getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("register1", "Image uploaded! Image path: ${it.metadata?.path}")

                ref.downloadUrl.addOnCompleteListener {
                    Log.d("register1", "File location: ${it.result}")

                    saveUserToDatabase(it.result.toString())
                    val intent = Intent(this, Register2Activity::class.java)

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears intent list bc don't want to be able to go back with back button
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                // Blank for the time being
                Toast.makeText(this, "Error with upload picture", Toast.LENGTH_SHORT).show()
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
                Log.d("register1", "User saved to Firebase Database")

            }
            .addOnFailureListener {
                // Blank for the time being
            }
    }

}

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String) : Parcelable {
    // No-argument constructor
    constructor() : this("","","")
}