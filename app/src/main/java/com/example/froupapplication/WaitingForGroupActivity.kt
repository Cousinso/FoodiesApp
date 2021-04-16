package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_food.*
import kotlinx.android.synthetic.main.activity_waiting_for_group.*
import java.util.*

class WaitingForGroupActivity : AppCompatActivity() {
    companion object {
        var currentFood: Food? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_group)

        currentFood = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)

        supportActionBar?.title = currentFood?.name

        if (currentFood?.foodImageUrl != "") {
            Picasso.get().load(currentFood?.foodImageUrl).into(foodImageImageViewWaitingForGroupActivity)
        }

        waitForGroup()
    }

    private fun waitForGroup () {
        val fid = currentFood?.fid
        val ref = FirebaseDatabase.getInstance().getReference("/foods/$fid/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("WaitingForGroupActivity", snapshot.ref.toString())
                Log.d("WaitingForGroupActivity", "Number of users in food group: ${snapshot.childrenCount}")

                if (snapshot.childrenCount >= 4) {
                    Log.d("WaitingForGroupActivity", "Creating group...")
                    createGroupChat()
                    // Need to clear food database node
                    // Need to go to group chat after 4 users found
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun createGroupChat(){
        val fid = currentFood?.fid
        val ref = FirebaseDatabase.getInstance().getReference("/foods/$fid/users")
        val ref1 = FirebaseDatabase.getInstance().getReference("/groupchats").push()
        val gcId = ref1.key as String

        if (currentFood?.foodImageUrl != "") {
            val groupChat = GroupChatClass(gcId, currentFood?.name.toString(), currentFood?.foodImageUrl.toString())
            ref1.setValue(groupChat)
        }

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshotChildren in snapshot.children) {
                    val user = snapshotChildren.getValue(userUidName::class.java)
                    Log.d("WaitingForGroupActivity", user?.uid.toString())
                    val ref2 = FirebaseDatabase.getInstance().getReference("/users/${user?.uid}/groupchats").push()
                    ref2.setValue(gcId)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        performCreationMessage(gcId)

        val intent = Intent(this, GroupChatLogActivity::class.java)
        intent.putExtra("GCID", gcId)
        startActivity(intent)
    }

    private fun performCreationMessage(gcid: String) {
        val text = "Groupchat created"

        val fromID = gcid
        val toID = gcid

        val reference = FirebaseDatabase.getInstance().getReference("/group-messages/$toID").push()

        val chatMessage = ChatMessage(reference.key!!, fromID, toID, System.currentTimeMillis() / 1000, text)

        reference.setValue(chatMessage)

        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-group-messages/$toID/message")
        latestMessageReference.setValue(chatMessage)
    }
}

private class userUidName(val uid: String, val name: String) {
    constructor() : this("", "")
}

private class GroupChatClass(val id: String,val name: String, val photoUri: String){
    constructor() : this("", "","")
}