package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_food.*

class SelectedFoodActivity : AppCompatActivity() {
    companion object {
        val TAG = "FoodSelection"
        val FOOD_KEY = "FOOD_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_food)

        val food = intent.getParcelableExtra<Food>(FoodSelectionActivity.FOOD_KEY)

        supportActionBar?.title = "Selected Food"
        if (food?.foodImageUrl != "") {
            Picasso.get().load(food?.foodImageUrl).into(foodImageViewSelectedFood)
        }
        descriptionTextViewSelectedFood.text = food?.name

        val currentUser = LatestMessagesActivity.currentUser

        val uid = currentUser?.uid
        val username = currentUser?.username

        Log.d("SelectedFoodActivity", "Current UID: $uid")
        Log.d("SelectedFoodActivity", "Current User: $username")

        confirmButtonSelectedFood.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/foods/${food?.fid}/group-users").push()

            val user = UserGroupItem(uid!!, username!!)
            ref.setValue(user)

            val intent = Intent(it.context, WaitingForGroupActivity::class.java)
            intent.putExtra(FOOD_KEY, food)
            startActivity(intent)
            finish()
        }
    }
}

private class UserGroupItem(val uid: String, val name: String){
    constructor() : this("", "")
}