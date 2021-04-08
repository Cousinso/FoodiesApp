package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_group_chat_latest_messages.*
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class GroupChatSelectionActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()

    val latestMessagesMap = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_latest_messages)

        supportActionBar?.title = "Group Chats"

        groupMessageRecyclerView.adapter = adapter
        groupMessageRecyclerView.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        )

        adapter.setOnItemClickListener { item, view ->
            Log.d("GroupChatActivity", "Groupchat pressed!")
            val intent = Intent(this, GroupChatLogActivity::class.java)

            val row = item as GroupChatItem

            intent.putExtra("GCID", row.toId)
            startActivity(intent)
        }

        getGcsFromDatabase()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_group_chat -> {
                val intent = Intent(this, NewGroupChatActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getGcsFromDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().uid}/groupchats")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                     Log.d("GroupChatLogActivity", it.toString())
                     val gcid = it.getValue(String::class.java)
                     if(gcid != null){
                         listenForLatestMessages(gcid)
                     }
                 }
            }
        })
    }


    private fun listenForLatestMessages(id: String){
        val ref = FirebaseDatabase.getInstance().getReference("/latest-group-messages/$id")
        Log.d("GroupChatLogActivity", "Starting listener on $id")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                Log.d("GroupChatLogActivity", "Got message in latest messages ${chatMessage}")
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }



    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            var gcname = ""
            val thisit = it
            FirebaseDatabase.getInstance().getReference("/groupchats/${it.toID}/name").get().addOnSuccessListener {
                Log.d("GroupChatLogActivity","Got ${it.value}")
                if(it.value != null){
                    gcname = it.value as String
                }
                FirebaseDatabase.getInstance().getReference("/groupchats/${thisit.toID}/photoUri").get().addOnSuccessListener {
                    Log.d("GroupChatLogActivity","Got ${it.value}")
                    var uri = ""
                    if(it.value != null){
                        uri = it.value as String
                    }
                    adapter.add(GroupChatItem(gcname,thisit.toID,thisit.text,uri))
                }
            }

        }
    }

}

class GroupChatItem(gcname: String,Id: String, text: String, image: String) : Item<GroupieViewHolder>() {
    val toId = Id
    val message = text
    val name = gcname
    val uri = image
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_latest_message.text = name
        viewHolder.itemView.latest_message_textview.text = message
        if(uri != ""){
            Picasso.get().load(uri).into(viewHolder.itemView.imageView_latest_message)
        }
        else{
            Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(viewHolder.itemView.imageView_latest_message)
        }
    }
    override fun getLayout() : Int {
        return R.layout.latest_messages_row
    }
}