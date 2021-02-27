package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItem())
        adapter.add(ChatItem())
        adapter.add(ChatItem())

        val recyclerView = findViewById<RecyclerView>(R.id.newMessageRecyclerView)
        recyclerView.adapter = adapter
    }
}

class ChatItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.message_from_row
    }
}