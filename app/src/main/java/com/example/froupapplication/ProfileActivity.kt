package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActivityChooserView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //var result = ""
    var food1 = ""
    var food2 = ""
    var food3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)

        var spinner1 = findViewById<Spinner>(R.id.foodSpinnerRegister1)
        var spinner2 = findViewById<Spinner>(R.id.foodSpinnerRegister2)
        var spinner3 = findViewById<Spinner>(R.id.foodSpinnerRegister3)
        val register = findViewById<TextView>(R.id.registerButtonProfile)

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
            spinner2.adapter = adapter
            spinner3.adapter = adapter
        }

        spinner1.onItemSelectedListener = this
        //Log.d("ProfileActivity", "Food1: $result")
        //food1 = result
        spinner2.onItemSelectedListener = this
        //Log.d("ProfileActivity", "Food1: $result")
        //food2 = result
        spinner3.onItemSelectedListener = this
        //Log.d("ProfileActivity", "Food1: $result")
        //food3 = result

        // Go to main activity
        register.setOnClickListener {

            Log.d("ProfileActivity", "Food1: $food1")
            Log.d("ProfileActivity", "Food2: $food2")
            Log.d("ProfileActivity", "Food3: $food3")

            val intent = Intent(this, LatestMessagesActivity::class.java)
            // Clears intent list and back button goes to home screen
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        //this.result = parent?.getItemAtPosition(pos).toString()
        this.food1 = parent?.getItemAtPosition(pos).toString()
        this.food2 = parent?.getItemAtPosition(pos).toString()
        this.food3 = parent?.getItemAtPosition(pos).toString()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        //result = "Please Select an Option"
        food1 = "Please Select an Option"
        food2 = "Please Select an Option"
        food3 = "Please Select an Option"
    }
}