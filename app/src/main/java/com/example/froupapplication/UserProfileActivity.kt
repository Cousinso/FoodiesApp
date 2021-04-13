package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_profile_self.*
import kotlinx.android.synthetic.main.activity_test_profile.*
import kotlinx.android.synthetic.main.chat_from_row.view.*

class UserProfileActivity : AppCompatActivity(){
    var currentUser: User? = null
    var personalInfo: ProfileInfo? = null
    var foodPreferences: FoodPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_self)

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("/users/${auth.uid}")
        val refPersonalInfo = database.getReference("/users/${auth.uid}/personalInfo")
        val refFoodPreferences = database.getReference("/users/${auth.uid}/foodPreferences")

        refUser.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("userprofile", "Current user: ${currentUser?.username}")
                val username = currentUser?.username
                val uri = currentUser?.profileImageUrl
                val imageView: ImageView = findViewById(R.id.selfImage)
                Picasso.get().load(uri).resize(200, 0).centerInside().into(imageView) //might be 150 instead of 200 idk yet
                //val personalInfo = currentUser?.personalInfo
                //Log.d("userprofile", "Current user personal info: ${currentUser?.personalInfo}")
                selfTextBio.text = "picturedone"
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user data")
            }

        })

        refPersonalInfo.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                personalInfo = snapshot.getValue(ProfileInfo::class.java)
                val bday = personalInfo?.bday
                val bio = personalInfo?.bio
                val gender = personalInfo?.gender
                val lifeActivity = personalInfo?.lifeActivity
                val location = personalInfo?.location
                val locationUse = personalInfo?.locationUse
                Log.d("userprofile", "Current user bday: $bday")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user personal info")
            }
        })

        refFoodPreferences.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                foodPreferences = snapshot.getValue(FoodPreferences::class.java)
                val allergies = foodPreferences?.allergies
                val foodPref = foodPreferences?.foodPref
                val mealPref = foodPreferences?.mealPref
                val meetPref = foodPreferences?.meetPref
                Log.d("userprofile", "Current user allergies: $allergies")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user food preferences")
            }
        })

        
    }


}

@Parcelize
class ProfileInfo(val bday: String, val bio: String, val gender: String, val lifeActivity : String , val location : String, val locationUse : String) : Parcelable {
    // No-argument constructor
    constructor() : this("","","", "", "","")
}

@Parcelize
class FoodPreferences(val allergies: String, val foodPref: String, val mealPref: String, val meetPref : String) : Parcelable {
    // No-argument constructor
    constructor() : this("","","", "")
}