package com.example.froupapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.green
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_group_chat_log.*
import kotlinx.android.synthetic.main.activity_new_group_chat.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import java.util.*


val groupChatList = mutableListOf(FirebaseAuth.getInstance().uid)
class NewGroupChatActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group_chat)

        supportActionBar?.title = "Add people"
        getUsersFromDatabase()

        val createChat = findViewById<Button>(R.id.newGroupChatConfirm)
        val selectPhoto = findViewById<Button>(R.id.photoButtonNewGroupChat)

        createChat.setOnClickListener{
            Log.d("NewGroupChatActivity","Pressed button to confirm groupchat")
            createGroupChat()
        }
        selectPhoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }


    }
    companion object {
        val USER_KEY = "USER_KEY"
    }
     private fun createGroupChat(){


         val ref1 = FirebaseDatabase.getInstance().getReference("/groupchats").push()
         val gcId = ref1.key as String
         val name = findViewById<EditText>(R.id.newGroupChatNameEditText).text.toString()
         val filename = UUID.randomUUID().toString()
         val storage = FirebaseStorage.getInstance()
         val ref = storage.getReference("/images/$filename")

         if(selectedPhotoUri == null){
             Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
             return
         }
         if(name == ""){
             Toast.makeText(this, "Please select a name", Toast.LENGTH_SHORT).show()
             return
         }
         if(groupChatList.size == 1){
             Toast.makeText(this, "Please add more users", Toast.LENGTH_SHORT).show()
             return
         }
         ref.putFile(selectedPhotoUri!!)
                 .addOnSuccessListener {
                     Log.d("RegisterActivity", "Image uploaded! Image path: ${it.metadata?.path}")

                     ref.downloadUrl.addOnCompleteListener {
                         Log.d("RegisterActivity", "File location: ${it.result}")
                         ref1.setValue(GroupChat(gcId,name,it.result.toString()))
                     }
                 }

         groupChatList.forEach{
             if(it != ""){
                 val ref2 = FirebaseDatabase.getInstance().getReference("/users/$it/groupchats").push()
                 ref2.setValue(gcId)
             }

         }

        performCreationMessage(gcId)

         val intent = Intent(this, GroupChatLogActivity::class.java)
         intent.putExtra("GCID", gcId)
         startActivity(intent)



     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // Check selected image
            Log.d("RegisterActivity", "Photo was selected")

            // Gives location of image data
            selectedPhotoUri = data.data

            // https://github.com/hdodenhof/CircleImageView
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val imageView = findViewById<CircleImageView>(R.id.photoimageViewNewGroupchat)
            imageView.setImageBitmap(bitmap)
            val photoButton = findViewById<Button>(R.id.photoButtonNewGroupChat)
            photoButton.alpha = 0f
        }
    }


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
                        view.setBackgroundColor(Color.WHITE)
                        groupChatList.remove(userItem.user.uid)
                        Log.d("NewGroupChatActivity","Removed " + userItem.user.username + "from groupchat")
                    }
                    else{
                        view.setBackgroundColor(Color.GREEN)
                        groupChatList.add(userItem.user.uid)
                        Log.d("NewGroupChatActivity","Added " + userItem.user.username + "to groupchat")
                    }
                }
                val recyclerView = findViewById<RecyclerView>(R.id.newMessageRecyclerView)
                recyclerView.adapter = adapter
            }
        })
    }

    private fun performCreationMessage(gcid: String) {
        val text = "Groupchat created"

        val fromID = FirebaseAuth.getInstance().uid
        val toID = gcid

        val reference = FirebaseDatabase.getInstance().getReference("/group-messages/$toID").push()
        if (fromID == null) return
        if (toID == null) return

        val chatMessage = ChatMessage(reference.key!!, fromID, toID, System.currentTimeMillis() / 1000, text)

        reference.setValue(chatMessage)

        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-group-messages/$toID/message")
        latestMessageReference.setValue(chatMessage)
    }
}

private class GroupChat(val id: String,val name: String, val photoUri: String){
    constructor() : this("", "","")
}


