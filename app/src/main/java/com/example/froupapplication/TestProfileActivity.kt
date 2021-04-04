package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_test_profile.*

class TestProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_profile)

        val user = intent.getParcelableExtra<User>("User")
        Picasso.get().load(user?.profileImageUrl).into(imageView)
        textView3.text = user?.username
    }
}