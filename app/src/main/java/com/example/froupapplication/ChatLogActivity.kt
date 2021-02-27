package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val chatLogSendButton = findViewById<Button>(R.id.sendButtonChatLog)

        supportActionBar?.title = "Chat Log"

        setupDummyData()

        chatLogSendButton.setOnClickListener{
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }
    }

    class ChatMessage(val text: String)
    //saves message to firebase
    private fun performSendMessage(){
        //I wasn't sure if this was the equivalent
        val text = messageEditTextChatLog.text.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage = ChatMessage(text)

        reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved chat message: ${reference.key}")
                }
    }

    private fun setupDummyData(){
        val adapter = GroupAdapter<GroupieViewHolder>()
        val recyclerview_chat_log = findViewById<RecyclerView>(R.id.chatLogRecyclerViewChatLog)

        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))

        recyclerview_chat_log.adapter = adapter

        val recyclerView = findViewById<RecyclerView>(R.id.newMessageRecyclerView)
        recyclerView.adapter = adapter
    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textView_from_row.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textView_to_row.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_to_row
    }
}