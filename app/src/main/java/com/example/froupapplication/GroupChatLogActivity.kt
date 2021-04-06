package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_group_chat_log.*

class GroupChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toId: String? = null

    var gcName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_log)
        val chatLogSendButton = findViewById<Button>(R.id.sendButtonGroupchatLog)
        groupchatLogRecyclerViewChatLog.adapter = adapter
        toId = intent.getStringExtra("GCID")
        Log.d("GroupChatLogActivity","toId " + toId)

        chatLogSendButton.setOnClickListener {
            performSendMessage()
        }
        getGcName()

    }

    private fun getGcName(){
        FirebaseDatabase.getInstance().getReference("/groupchats/$toId").child("name").get().addOnSuccessListener {
            Log.d("GroupChatLogActivity","Got ${it.value}")
            if(it.value != null){
                gcName = it.value as String
            }
            supportActionBar?.title = gcName
        }
    }

    private fun performSendMessage() {
        val text = findViewById<EditText>(R.id.messageEditTextGroupchatLog).text.toString()

        val fromID = FirebaseAuth.getInstance().uid
        val toID = toId

        val reference = FirebaseDatabase.getInstance().getReference("/group-messages/$toId").push()
        if (fromID == null) return
        if (toID == null) return

        val chatMessage = ChatMessage(reference.key!!, fromID, toID, System.currentTimeMillis() / 1000, text)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(ChatLogActivity.TAG, "Saved chat message: ${reference.key}")
                messageEditTextChatLog.text.clear()
                chatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID")
        latestMessageReference.setValue(chatMessage)
    }
}