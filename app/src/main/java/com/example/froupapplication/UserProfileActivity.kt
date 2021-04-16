package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_profile_self.*

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
        val refFoodTypes = database.getReference("/users/${auth.uid}/foodTypes")
        val refAllergies = database.getReference("/users/${auth.uid}/allergies")

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
                selfUserName.text = username
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user data")
            }

        })

        refPersonalInfo.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val personalInfo = snapshot.getValue(ProfileInfo::class.java)
                val adress = personalInfo?.adress
                textView_Location.text = adress
                val bday = personalInfo?.bday
                selfBirthday.text = bday
                val bio = personalInfo?.bio
                selfTextBio.text = bio
                val city = personalInfo?.city
                textView_Location3.text = city
                val country = personalInfo?.country
                textView_Location2.text = country
                val gender = personalInfo?.gender
                textView_LifeActivity2.text = gender
                val zip = personalInfo?.zip
                textView_Location4.text = zip
                Log.d("userprofile", "Current user bday: $bday")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user personal info")
            }
        })

        refFoodPreferences.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("userprofile", "snapshot is ${snapshot}")
                val foodPreferences = snapshot.getValue(FoodPreferences::class.java)
                Log.d("userprofile", "foodPreferences is ${foodPreferences}")
                val lifeActivity = foodPreferences?.lifeActitivity
                Log.d("userprofile", "lifeActivity is ${lifeActivity}")
                textView_LifeActivity.text = lifeActivity
                val mealPref = foodPreferences?.mealPreferences
                textview_MealPreferences.text = mealPref
                val meetPref = foodPreferences?.meatPreferences
                textview_MealPreferences2.text = meetPref
                Log.d("userprofile", "Current user lifeActivity: $lifeActivity")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user food preferences")
            }
        })

        refFoodTypes.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val foodTypes = snapshot.getValue(FoodTypes::class.java)
                val foodTypesItems = foodTypes?.items
                textView_LifeActivity3.text = foodTypesItems
                Log.d("userprofile", "Current user foodTypesItems: $foodTypesItems")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user food preferences")
            }
        })
        refAllergies.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val allergies = snapshot.getValue(Allergies::class.java)
                val allergiesItems = allergies?.items
                textView_Allergies.text = allergiesItems
                Log.d("userprofile", "Current user allergiesItems: $allergiesItems")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Could not fetch current user food preferences")
            }
        })

        selfHomeButton.setOnClickListener {
            val intent = Intent(this, FoodSelectionActivity::class.java)
            startActivity(intent)
        }

        selfEditButton.setOnClickListener {
            val intent = Intent(this, Register2Activity::class.java)
            startActivity(intent)
        }
    }


}

@Parcelize
class ProfileInfo(val adress: String, val bday: String, val bio: String, val city : String , val country : String, val gender : String, val zip : String) : Parcelable {
    // No-argument constructor
    constructor() : this("","","", "", "","","")
}

@Parcelize
class FoodPreferences(val lifeActitivity: String, val mealPreferences: String, val meatPreferences: String) : Parcelable {
    // No-argument constructor
    constructor() : this("","","")

}



