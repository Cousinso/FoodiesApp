package com.example.froupapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_group_chat_selection.*

class GroupChatSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_selection)

        supportActionBar?.title = "Group Chat Selection"

        getUsersFromDatabase()

        createGroupButtonGroupChatSelection.setOnClickListener {
            Log.d("GroupChatSelection", "Create Group pressed!")

        }
    }

    companion object {
        val GROUP_KEY = "GROUP_KEY"
        val selectedUsersList = mutableListOf(FirebaseAuth.getInstance().uid)
    }

    private fun getUsersFromDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    Log.d("GroupChatSelection", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                // Selection of users to be put in a group
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    Log.d("GroupChatSelection", "Selected:" + userItem.user.username)
                    selectedUsersList.add(userItem.user.uid)
                    view.setBackgroundColor(Color.YELLOW)

                }

                val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerViewGroupChatSelection)
                recyclerView.adapter = adapter
            }
        })
    }
}