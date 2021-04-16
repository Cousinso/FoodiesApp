package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        getUsersFromDatabase()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun getUsersFromDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    Log.d("NewGroupChatActivity", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                // Sends user to chat logs with a person
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    // Sends selected user data to ChatLogActivity
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }

                val recyclerView = findViewById<RecyclerView>(R.id.newMessageRecyclerView)
                recyclerView.adapter = adapter
            }
        })
    }
}

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.usernameTextViewNewMessage.text = user.username
        if(user.profileImageUrl == ""){
            Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(viewHolder.itemView.photoImageViewNewMessages)
        }
        else{
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.photoImageViewNewMessages)
        }

    }


    override fun getLayout() : Int {
        return R.layout.user_row_new_message
    }
}