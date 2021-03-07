package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_profile_test.*

class ProfileTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_test)
        //val nameText = findViewById<TextView>(R.id.userProfileName)
//        //val i = Intent.getIntentOld("User ")
//        val newText = intent.getStringExtra("User").toString()
//        nameText.text = newText
        val user = intent.getParcelableExtra<User>("User")
        userProfileName.text = user?.username

        Log.d("ProfileTest", "Username: ${userProfileName.toString()}")

    }
}