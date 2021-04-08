package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_test.*
import kotlinx.android.synthetic.main.activity_selected_food.*

class ProfileTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_test)

        val user = intent.getParcelableExtra<User>("User")
        if (user?.profileImageUrl != "") {
            Picasso.get().load(user?.profileImageUrl).into(pictureImageViewProfile)
        }

        usernameTextViewProfile.text = user?.username

        Log.d("ProfileTest", "Username: ${user?.username}")

    }
}