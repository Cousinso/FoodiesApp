package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register_profile_2.*

class RegisterFoodPreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile_2)

        //Handling the foodPreferences checkboxes
        var foodPreferencesForDB = ""
        ChFood.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                foodPreferencesForDB += "Chinese food, "
            }
            else {
                foodPreferencesForDB = foodPreferencesForDB.replace("Chinese food, ", "")
            }
        }
        IrFood.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                foodPreferencesForDB += "Irish food, "
            }
            else {
                foodPreferencesForDB = foodPreferencesForDB.replace("Irish food, ", "")
            }
        }
        BelgFood.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                foodPreferencesForDB += "Belgian food, "
            }
            else {
                foodPreferencesForDB = foodPreferencesForDB.replace("Belgian food, ", "")
            }
        }
        JapFood.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                foodPreferencesForDB += "Japanese food, "
            }
            else {
                foodPreferencesForDB = foodPreferencesForDB.replace("Japanese food, ", "")
            }
        }
        InFood.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                foodPreferencesForDB += "Indian food, "
            }
            else {
                foodPreferencesForDB = foodPreferencesForDB.replace("Indian food, ", "")
            }
        }

        //Handling the Allergies checkboxes
        var allergiesForDB = ""
        milk.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                allergiesForDB += "milk, "
            }
            else {
                allergiesForDB = allergiesForDB.replace("milk, ", "")
            }
        }
        lactose.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                allergiesForDB += "lactose, "
            }
            else {
                allergiesForDB = allergiesForDB.replace("lactose, ", "")
            }
        }
        fish.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                allergiesForDB += "fish, "
            }
            else {
                allergiesForDB = allergiesForDB.replace("fish, ", "")
            }
        }
        nuts.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                allergiesForDB += "nuts, "
            }
            else {
                allergiesForDB = allergiesForDB.replace("nuts, ", "")
            }
        }
        pork.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                allergiesForDB += "pork, "
            }
            else {
                allergiesForDB = allergiesForDB.replace("pork, ", "")
            }
        }

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


        ButtonNext.setOnClickListener {
            //lateinit var database: DatabaseReference
            //database = Firebase.database.reference
            //val user = Firebase.auth.currentUser

            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("/users/${auth.uid}")

            val pref = Preferences(foodPreferencesForDB, allergiesForDB, meatPreferencesForDB, mealPreferencesForDB)
            ref.child("foodPreferences").setValue(pref)
                .addOnSuccessListener {
                    Log.d("RegisterFoodAct", "foodPreferences saved for user ${auth.uid} to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("RegisterFoodAct", "Error : foodPreferences not saved for user ${auth.uid}  to Firebase Database")
                }

            val intent = Intent(this, FoodSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears intent list bc we leave registration
            startActivity(intent)
        }

    }
}

@Parcelize
class Preferences(val foodPreferences: String, val allergies: String, val meatPreferences: String, val mealPreferences: String) :
    Parcelable {
}