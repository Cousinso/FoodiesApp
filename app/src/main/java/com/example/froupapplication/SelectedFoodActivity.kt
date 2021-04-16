package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_food.*

class SelectedFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_food)

        val food = intent.getParcelableExtra<Food>(FoodSelectionActivity.FOOD_KEY)

        supportActionBar?.title = "Selected Food"
        Picasso.get().load(food?.foodImageUrl).into(foodImageViewSelectedFood)
        descriptionTextViewSelectedFood.text = food?.name

        val currentUser = LatestMessagesActivity.currentUser
        val auth = FirebaseAuth.getInstance()
        val uid = currentUser?.uid
        val username = currentUser?.username

        Log.d("SelectedFoodActivity", "Current UID: $uid")
        Log.d("SelectedFoodActivity", "Current User: $username")

        confirmButtonSelectedFood.setOnClickListener {
            //val intent = Intent(it.context, NewMessageActivity::class.java)
//            val intent = Intent(it.context, SwipeActivity::class.java)
            val ref = FirebaseDatabase.getInstance().getReference("/foods/${food?.fid}/users").push()

            val user = LatestMessagesActivity.currentUser
            ref.setValue(user)
            val intent = Intent(it.context, GroupVsPairActivity::class.java)
            var foodRef = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid?:"").child("food")
            user?.food = food?.name.toString()
            foodRef.setValue(user?.food)
            intent.putExtra("food", FoodSelectionActivity.FOOD_KEY)
            startActivity(intent)
            finish()
        }
    }
}