package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_food.*
import kotlinx.android.synthetic.main.activity_waiting_for_group.*

class WaitingForGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_group)

        val food = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)

        supportActionBar?.title = food?.name

        if (food?.foodImageUrl != "") {
            Picasso.get().load(food?.foodImageUrl).into(foodImageImageViewWaitingForGroupActivity)
        }
    }
}