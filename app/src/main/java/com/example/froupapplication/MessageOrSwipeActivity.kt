package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class MessageOrSwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_or_swipe)

        val message = findViewById<Button>(R.id.messageButton)
        val swipe = findViewById<Button>(R.id.swipeButton)
        val text = findViewById<TextView>(R.id.UserTextView)
        text.setText("uid")
        message.setOnClickListener{
            var toUser: User? = null
            FirebaseDatabase.getInstance().getReference("/users/$uid").get().addOnSuccessListener {
                Log.d("GroupChatLogActivity","Got ${it.value}")
                var uri = ""
                if(it.value != null){
                    val map = it.value as HashMap<*,*>
                    Log.d("GroupChatLogActivity","Clicker profile, got map $map")
                    val toUser = User(map.get("uid").toString(),map.get("username").toString(),map.get("profileImageUrl").toString())

                    val myContext = context
                    val intent = Intent(myContext, ProfileTestActivity::class.java)
                    intent.putExtra("User", toUser)
                    myContext.startActivity(intent)
                }

            }
        }
        swipe.setOnClickListener{
            finish()
        }
    }

    private fun getUserAndOpenChat(){

    }

}