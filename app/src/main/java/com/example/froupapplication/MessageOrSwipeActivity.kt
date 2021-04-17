package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MessageOrSwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_or_swipe)

        val message = findViewById<Button>(R.id.messageButton)
        val swipe = findViewById<Button>(R.id.swipeButton)
        val text = findViewById<TextView>(R.id.UserTextView)
        text.setText("uid")
        message.setOnClickListener{
            val intent = Intent(this, ChatLogActivity::class.java)


            intent.putExtra(NewMessageActivity.USER_KEY, otherUser)
            startActivity(intent)
            startActivity(intent)
        }
        swipe.setOnClickListener{
            finish()
        }
    }

    private fun getUserAndOpenChat(){

    }

}