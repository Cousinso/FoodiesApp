package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val chatLogSendButton = findViewById<Button>(R.id.sendButtonChatLog)

        // Grabs selected user from NewMessageActivity
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        setupDummyData()

        chatLogSendButton.setOnClickListener{
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }
    }

    class ChatMessage(val text: String)
    //saves message to firebase
    private fun performSendMessage(){
        val text = findViewById<EditText>(R.id.messageEditTextChatLog).text.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage = ChatMessage(text)

        reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved chat message: ${reference.key}")
                }
    }

    private fun setupDummyData(){
        val adapter = GroupAdapter<GroupieViewHolder>()
        val chatLogRecyclerView = findViewById<RecyclerView>(R.id.chatLogRecyclerViewChatLog)

        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))

        chatLogRecyclerView.adapter = adapter
    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textViewChatFromRow.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textViewChatToRow.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_to_row
    }
}