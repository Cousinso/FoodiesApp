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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_group_chat_selection.*
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class GroupChatSelectionActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_selection)

        supportActionBar?.title = ""

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
        supportActionBar?.title = "Group Chats"
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
                     Log.d("NewMessageActivity", it.toString())
                     val gcid = it.getValue(String::class.java)
                     var gcname = ""
                     FirebaseDatabase.getInstance().getReference("/groupchats/$gcid").child("name").get().addOnSuccessListener {
                         Log.d("GroupChatActivity", "Got value ${it.value}")
                         gcname = it.value as String
                         Log.d("GroupChatActivity", "Set value $gcname")
                         if (gcname != null && gcid != null) {
                             adapter.add(GroupChatItem(gcid,gcname))
                             Log.d("GroupChatActivity", "Added $gcname to adapter")
                         }
                    }


                }




                val recyclerView = findViewById<RecyclerView>(R.id.groupMessageRecyclerView)
                recyclerView.adapter = adapter
            }
        })
    }

}

class GroupChatItem(val Id: String, val gcName: String) : Item<GroupieViewHolder>() {
    val name = gcName
    val toId = Id
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_latest_message.text = name
        viewHolder.itemView.latest_message_textview.text = name
        Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(viewHolder.itemView.imageView_latest_message)
    }
    override fun getLayout() : Int {
        return R.layout.latest_messages_row
    }
}