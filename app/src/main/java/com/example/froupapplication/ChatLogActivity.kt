package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupieViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        setupDummyData()
    }
    private fun setupDummyData(){
        val adapter = GroupAdapter<GroupieViewHolder>()
        val recyclerview_chat_log = findViewById<RecyclerView>(R.id.chatLogRecyclerViewChatLog)

        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))

        recyclerview_chat_log.adapter = adapter

    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textView_from_row.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.textView_to_row.text = text
    }
    override fun getLayout(): Int{
        return R.layout.chat_to_row
    }
}