package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        confirmButtonSelectedFood.setOnClickListener {
            val intent = Intent(it.context, NewMessageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}