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

        //Handling the location info
//        var locationUse = ""
//        register_radioGroup_location.setOnCheckedChangeListener { group, checkedId ->
//            if (checkedId == R.id.register_radio_only){
//                locationUse = "Location can only be used when app is used"
//            }
//            if (checkedId == R.id.register_radio_always){
//                locationUse = "Location can always be used"
//            }
//        }

        //Handling the work info
        var lifeActivity = ""
        register_radioGroup_work.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioWork){
                lifeActivity = "College"
            }
            if (checkedId == R.id.radioCollege){
                lifeActivity = "Work"
            }
        }





        register_ButtonNext.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("/users/${auth.uid}")

            val birthday = Register_Birthday.text.toString()
            val bio = editTextBio.text.toString()
            val location = editLocation.text.toString()

            val personalInfo = PersonalInfo(birthday, gender, bio, location, lifeActivity)
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
class PersonalInfo(val bday: String, val gender: String, val bio: String, val location: String, val lifeActivity: String) :
    Parcelable {
}

private fun SearchView.setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener, function: () -> Unit) {

}
