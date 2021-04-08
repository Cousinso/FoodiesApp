package com.example.froupapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    var otherPersonUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        val otherPersonID: String
        if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
            otherPersonID = chatMessage.toID
        }
        else {
            otherPersonID = chatMessage.fromID
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$otherPersonID")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                otherPersonUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.username_textview_latest_message.text = otherPersonUser?.username
                val targetImage = viewHolder.itemView.imageView_latest_message
                if(otherPersonUser?.profileImageUrl != ""){
                    Picasso.get().load(otherPersonUser?.profileImageUrl).into(targetImage)
                }
                else{
                    Picasso.get().load("https://miro.medium.com/max/800/0*evjjYzmFhBV-djWJ.jpg").into(targetImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        viewHolder.itemView.latest_message_textview.text = chatMessage.text
    }
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }
}