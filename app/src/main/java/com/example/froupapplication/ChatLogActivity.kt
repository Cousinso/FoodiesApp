package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        setupDummyData()
    }
    private fun setupDummyData(){
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatFromItem())
        //adapter.add(chatToItem())

        //recyclerview_chat_log.adapter = adapter

    }
}

class ChatFromItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textView_from_row.text = "From message "
    }
}