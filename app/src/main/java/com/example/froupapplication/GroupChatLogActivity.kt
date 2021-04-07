package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_group_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class GroupChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toId: String? = null

    var gcName = ""

    val myId = FirebaseAuth.getInstance().uid

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

        listenForMessages()

    }

    private fun listenForMessages() {

        val ref = FirebaseDatabase.getInstance().getReference("/group-messages/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(ChatLogActivity.TAG, chatMessage.text)
                    if (chatMessage.fromID == myId) {
                        adapter.add(ChatFromItem(chatMessage.text, myId))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, chatMessage.fromID))
                    }
                    Log.d("GroupChatLogActivity","got message ${chatMessage.text} from ${chatMessage.fromID}")
                }

                groupchatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
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
                messageEditTextGroupchatLog.text.clear()
                groupchatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/message")
        latestMessageReference.setValue(chatMessage)
    }

    class ChatFromItem(val text: String, val uid: String) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textViewChatFromRow.text = text

            // Load user image into chat log
            var uri = ""
            FirebaseDatabase.getInstance().getReference("/users/$uid").child("profileImageUrl").get().addOnSuccessListener {
                Log.d("GroupChatLogActivity","Got ${it.value}")
                if(it.value != null){
                    uri = it.value as String
                }
                val target = viewHolder.itemView.photoImageViewChatFromRow
                if(uri != ""){
                    Picasso.get().load(uri).into(target)
                }
                else{
                    Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(target)
                }
            }


        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }

    }

    class ChatToItem(val text: String, val uid: String) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textViewChatToRow.text = text

            // Load user image into chat log
            var uri = ""
            FirebaseDatabase.getInstance().getReference("/users/$uid").child("profileImageUrl").get().addOnSuccessListener {
                Log.d("GroupChatLogActivity","Got ${it.value}")
                if(it.value != null){
                    uri = it.value as String
                }
                val target = viewHolder.itemView.photoImageViewChatToRow
                if(uri != ""){
                    Picasso.get().load(uri).into(target)
                }
                else{
                    Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(target)
                }
            }

        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }
    }
}