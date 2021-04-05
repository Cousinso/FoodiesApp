package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GroupChatSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_selection)

        supportActionBar?.title = "Group Chat Selection"
    }
}