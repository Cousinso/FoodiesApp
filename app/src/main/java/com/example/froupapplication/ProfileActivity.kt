package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class ProfileActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var food1 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)

        var spinner1 = findViewById<Spinner>(R.id.foodSpinnerRegister1)
        val register = findViewById<TextView>(R.id.registerButtonProfile)

        val bio = findViewById<EditText>(R.id.editTextBio)

        // Create an ArrayAdapter using the string array
        ArrayAdapter.createFromResource(
                this,
                R.array.foodTypes,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinners
            spinner1.adapter = adapter
        }

        spinner1.onItemSelectedListener = this


        /*val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            
            user.food = food1
            user.Bio = bio.text.toString()
        }

        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ProfileActivity", "User profile updated.")
            }
        }*/


        // Go to main activity
        register.setOnClickListener {
            Log.d("ProfileActivity", "Food: $food1")
            Log.d("ProfileActivity", "Bio: ${bio.text.toString()}")

            val intent = Intent(this, LatestMessagesActivity::class.java)
            // Clears intent list and back button goes to home screen
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        //this.result = parent?.getItemAtPosition(pos).toString()
        this.food1 = parent?.getItemAtPosition(pos).toString()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        //result = "Please Select an Option"
        food1 = "Please Select an Option"
    }
}