package com.example.froupapplication

import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

//Github test
//hii
//hey
class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLogActivity"
    }

    //Allows objects to be added to recycler view adapter
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val chatLogSendButton = findViewById<Button>(R.id.sendButtonChatLog)
        val chatFromImage = findViewById<ImageView>(R.id.photoImageViewChatFromRow)

        chatLogRecyclerViewChatLog.adapter = adapter
        // Grabs selected user from NewMessageActivity
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        //No more dummies. Smart people only
        //setupDummyData()

        listenForMessages()

        chatLogSendButton.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }
        //adapter.setOnItemClickListener { item, view ->
//      //      val userItem = item as ChatToItem
        //    val intent = Intent(view.context, ProfileTestActivity::class.java)
        //    intent.putExtra("User", toUser)
        //    startActivity(intent)
        //}

    }

    private fun listenForMessages() {
        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser,this@ChatLogActivity))
                    }
                }

                chatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
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


    //saves message to firebase
    private fun performSendMessage() {
        val text = findViewById<EditText>(R.id.messageEditTextChatLog).text.toString()
//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val fromID = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //not sure why it needs a ?
        val toID = user?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toID/$fromID").push()
        if (fromID == null) return
        if (toID == null) return

        val chatMessage = ChatMessage(reference.key!!, fromID, toID, System.currentTimeMillis() / 1000, text)

        reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved chat message: ${reference.key}")
                    messageEditTextChatLog.text.clear()
                    chatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
                }
        toReference.setValue(chatMessage)

        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID/$toID")
        latestMessageReference.setValue(chatMessage)

        val latestMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/$fromID")
        latestMessageToReference.setValue(chatMessage)

//    private fun setupDummyData(){
//        val adapter = GroupAdapter<GroupieViewHolder>()
//        val chatLogRecyclerView = findViewById<RecyclerView>(R.id.chatLogRecyclerViewChatLog)
//
//        adapter.add(ChatFromItem("From message"))
//        adapter.add(ChatToItem("To message"))
//
//        chatLogRecyclerView.adapter = adapter
//    }
    }

    class ChatFromItem(val text: String, val user: User?) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textViewChatFromRow.text = text

            // Load user image into chat log
            val uri = user?.profileImageUrl
            val target = viewHolder.itemView.photoImageViewChatFromRow
            if(uri != ""){
                Picasso.get().load(uri).into(target)
            }
            else{
                Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(target)
            }

        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }

    }

    class ChatToItem(val text: String, val user: User?, val context: Context) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textViewChatToRow.text = text

            // Load user image into chat log
            val uri = user?.profileImageUrl
            val target = viewHolder.itemView.photoImageViewChatToRow
            if(uri != ""){
                Picasso.get().load(uri).into(target)
            }
            else{
                Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(target)
            }
            viewHolder.itemView.photoImageViewChatToRow.setOnClickListener{


                val myContext = context
                val intent = Intent(myContext, ProfileTestActivity::class.java)
                intent.putExtra("User", user)
                myContext.startActivity(intent)


            }
        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }
    }
}