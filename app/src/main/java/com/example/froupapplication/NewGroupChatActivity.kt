package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_group_chat.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


val groupChatList = mutableListOf(FirebaseAuth.getInstance().uid)
class NewGroupChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group_chat)

        supportActionBar?.title = "Add people"
        getUsersFromDatabase()

        val createChat = findViewById<FloatingActionButton>(R.id.button_confirm_new_groupchat)

        createChat.setOnClickListener{
            Log.d("NewGroupChatActivity","Pressed button to confirm groupchat")
            createGroupChat()
            openGroupChat()
        }

    }
    companion object {
        val USER_KEY = "USER_KEY"
    }
     private fun createGroupChat(){


         val ref1 = FirebaseDatabase.getInstance().getReference("/groupchats").push()
         val gcId = ref1.key as String
         ref1.setValue(GroupChat(gcId,"blah"))
         groupChatList.forEach{
             if(it != ""){
                 val ref = FirebaseDatabase.getInstance().getReference("/users/$it/groupchats").push()
                 ref.setValue(gcId)
             }

         }




     }

    private fun openGroupChat(){}

    private fun getUsersFromDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    Log.d("NewMessageActivity", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().uid) {
                        adapter.add(UserItem(user))
                    }
                }

                // Sends user to chat logs with a person
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    // Sends selected user data to ChatLogActivity
                    if(groupChatList.contains(userItem.user.uid)){
                        groupChatList.remove(userItem.user.uid)
                        Log.d("NewGroupChatActivity","Removed " + userItem.user.username + "from groupchat")
                    }
                    else{
                        groupChatList.add(userItem.user.uid)
                        Log.d("NewGroupChatActivity","Added " + userItem.user.username + "to groupchat")
                    }
                }

                val recyclerView = findViewById<RecyclerView>(R.id.newMessageRecyclerView)
                recyclerView.adapter = adapter
            }
        })
    }
}

private class GroupChat(val id: String,val name: String){
    constructor() : this("", "",)
}


