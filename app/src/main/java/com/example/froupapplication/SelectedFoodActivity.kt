package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        confirmButtonSelectedFood.setOnClickListener {
            val intent = Intent(it.context, WaitingForGroupActivity::class.java)
            intent.putExtra(FOOD_KEY, food)
            startActivity(intent)
            finish()
        }
    }
}