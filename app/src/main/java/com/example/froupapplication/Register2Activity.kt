package com.example.froupapplication

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register_profile.*


class Register2Activity: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)


        //Handling the gender
        var gender = ""
        register_radioGroup_Gender.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_male){
                gender = "man"
            }
            if (checkedId == R.id.radio_female){
                gender = "female"
            }
            if (checkedId == R.id.radio_other){
                gender = "other"
            }
        }


        register_ButtonNext.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("/users/${auth.uid}")

            val birthday = Register_Birthday.text.toString()
            val bio = editTextBio.text.toString()
            val adress = register_adress.text.toString()
            var country = register_country.text.toString()
            var city = register_city.text.toString()
            var zip = register_zip.text.toString()

            val personalInfo = PersonalInfo(birthday, gender, bio, adress, country,city, zip)
            ref.child("personalInfo").setValue(personalInfo)
                .addOnSuccessListener {
                    Log.d("register2", "Personal info saved for user ${auth.uid} to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("register2", "Error : Personal info not saved for user ${auth.uid}  to Firebase Database")
                }

            val intent = Intent(this, Register3Activity::class.java)
            startActivity(intent)
        }
    }
}

@Parcelize
class PersonalInfo(val bday: String, val gender: String, val bio: String, val adress: String, val country: String, val city: String, val zip: String) :
    Parcelable {
}

private fun SearchView.setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener, function: () -> Unit) {

}
