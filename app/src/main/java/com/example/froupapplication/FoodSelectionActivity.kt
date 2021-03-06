package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FoodSelectionActivity : AppCompatActivity() {
    companion object {
        val TAG = "FoodSelection"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_selection)
    }
}