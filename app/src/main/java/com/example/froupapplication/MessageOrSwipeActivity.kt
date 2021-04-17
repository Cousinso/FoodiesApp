package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize

class MessageOrSwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_or_swipe)
        val uid = intent.getStringExtra("User")

        val message = findViewById<Button>(R.id.messageButton)
        val swipe = findViewById<Button>(R.id.swipeButton)
        val text = findViewById<TextView>(R.id.UserTextView)
        val image = findViewById<ImageView>(R.id.imageViewUserProfile)

        var toUser: User? = null
        FirebaseDatabase.getInstance().getReference("/users/$uid").get().addOnSuccessListener {
            Log.d("GroupChatLogActivity","Got ${it.value}")
            var uri = ""
            if(it.value != null){
                val map = it.value as HashMap<*,*>
                toUser = User(map.get("uid").toString(),map.get("username").toString(),map.get("profileImageUrl").toString(),map.get("food").toString(),map.get("bio").toString())
                Picasso.get().load(toUser!!.profileImageUrl).into(image)
                text.setText(toUser!!.username)

            }
        }




        message.setOnClickListener{
                val intent = Intent(this, ChatLogActivity::class.java)
                intent.putExtra(NewMessageActivity.USER_KEY, toUser)
                startActivity(intent)
        }
        swipe.setOnClickListener{
            finish()
        }
    }

    private fun getUserAndOpenChat(){

    }

}