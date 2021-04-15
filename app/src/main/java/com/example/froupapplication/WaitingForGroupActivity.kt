package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_food.*
import kotlinx.android.synthetic.main.activity_waiting_for_group.*

class WaitingForGroupActivity : AppCompatActivity() {
    companion object {
        val currentFood: Food? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_group)

        val currentFood = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)

        supportActionBar?.title = currentFood?.name

        if (currentFood?.foodImageUrl != "") {
            Picasso.get().load(currentFood?.foodImageUrl).into(foodImageImageViewWaitingForGroupActivity)
        }

        waitForGroup()
    }

    private fun waitForGroup () {
        val fid = currentFood?.fid
        val ref = FirebaseDatabase.getInstance().getReference("/foods/$fid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("WaitingForGroupActivity", "Number of users in food group: ${snapshot.childrenCount}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}