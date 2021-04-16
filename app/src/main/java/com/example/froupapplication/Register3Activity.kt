package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register3.*

class Register3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register3)

        //Handling the meat preferences
        var meatPreferencesForDB = ""
        
        register_radioGroup_Meat.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_vegan){
                meatPreferencesForDB = "vegan"
            }
            if (checkedId == R.id.radio_vegatarian){
                meatPreferencesForDB = "vegatarian"
            }
            if (checkedId == R.id.radio_meat){
                meatPreferencesForDB = "meat lover"
            }
        }

        //Handling the mealpreferences checkboxes
        var mealPreferencesForDB = ""
        dinner.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                mealPreferencesForDB += "dinner, "
            }
            else {
                mealPreferencesForDB = mealPreferencesForDB.replace("dinner, ", "")
            }
        }
        lunch.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                mealPreferencesForDB += "lunch, "
            }
            else {
                mealPreferencesForDB = mealPreferencesForDB.replace("lunch, ", "")
            }
        }
        breakfast.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                mealPreferencesForDB += "breakfast, "
            }
            else {
                mealPreferencesForDB = mealPreferencesForDB.replace("breakfast, ", "")
            }
        }
        brunch.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                mealPreferencesForDB += "brunch, "
            }
            else {
                mealPreferencesForDB = mealPreferencesForDB.replace("brunch, ", "")
            }
        }
        teaCoffee.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                mealPreferencesForDB += "Tea or Coffee, "
            }
            else {
                mealPreferencesForDB = mealPreferencesForDB.replace("Tea or Coffee, ", "")
            }
        }


        var lifeActivity = ""
        register_radioGroup_work.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioWork){
                lifeActivity = "College"
            }
            if (checkedId == R.id.radioCollege){
                lifeActivity = "Work"
            }
        }


        val Back: Button = findViewById(R.id.Back)
        Back.setOnClickListener {
            val intent = Intent(this, Register2Activity::class.java)
            startActivity(intent)
        }
        ButtonFinish.setOnClickListener {

            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("/users/${auth.uid}")

            val pref = Preferences(meatPreferencesForDB, mealPreferencesForDB,lifeActivity)
            ref.child("foodPreferences").setValue(pref)
                .addOnSuccessListener {
                    Log.d("register3", "foodPreferences saved for user ${auth.uid} to Firebase Database")
//                    Toast.makeText(this, "Registration done", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d("register3", "Error : foodPreferences not saved for user ${auth.uid}  to Firebase Database")
                }


            val intent = Intent(this, Register4Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears intent list bc don't want to be able to go back with back button
            startActivity(intent)
        }

    }
}

@Parcelize
class Preferences(val meatPreferences: String, val mealPreferences: String, val lifeActitivity: String) :
    Parcelable {
}