package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_group_vs_pair.*

class GroupVsPairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_vs_pair)

        pairButton.setOnClickListener {
            val intent = Intent(it.context, SwipeActivity::class.java)
            startActivity(intent)
            finish()
        }
        groupButton.setOnClickListener{
            val intent = Intent(it.context, LatestMessagesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}