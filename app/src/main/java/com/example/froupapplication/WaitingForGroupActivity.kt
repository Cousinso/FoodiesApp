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
        var currentFood: Food? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_group)

        currentFood = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)

        supportActionBar?.title = currentFood?.name

        if (currentFood?.foodImageUrl != "") {
            Picasso.get().load(currentFood?.foodImageUrl).into(foodImageImageViewWaitingForGroupActivity)
        }

        waitForGroup()
    }

    private fun waitForGroup () {
        val fid = currentFood?.fid
        Log.d("WaitingForGroupActivity", fid.toString())
        val ref = FirebaseDatabase.getInstance().getReference("/foods/$fid/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("WaitingForGroupActivity", snapshot.ref.toString())
                Log.d("WaitingForGroupActivity", "${snapshot.childrenCount}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}