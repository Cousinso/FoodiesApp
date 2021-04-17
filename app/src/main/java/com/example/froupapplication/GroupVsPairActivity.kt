package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_group_vs_pair.*

class GroupVsPairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_vs_pair)

        val food = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)

        val currentUser = LatestMessagesActivity.currentUser
        //val user = LatestMessagesActivity.currentUser
        val fid = food?.fid


        val uid = currentUser?.uid
        val username = currentUser?.username

        pairButton.setOnClickListener {
            val intent = Intent(it.context, SwipeActivity::class.java)
            setFoodUser(food,currentUser)
            intent.putExtra("foodid",fid)
            startActivity(intent)
            finish()
        }
        groupButton.setOnClickListener{
            val ref = FirebaseDatabase.getInstance().getReference("/foods/${food?.fid}/group-users").push()

            val user = UserGroupItem(uid!!, username!!)
            ref.setValue(user)

            val intent = Intent(it.context, WaitingForGroupActivity::class.java)
            intent.putExtra(SelectedFoodActivity.FOOD_KEY, food)
            startActivity(intent)
            finish()
        }
    }
    private fun setFoodUser(food: Food?, currentUser: User?){
        val ref1 = FirebaseDatabase.getInstance().getReference("/foods/${food?.fid}/users")



        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {
                    //Log.d("NewMessageActivity", it.toString())
                    Log.d("SwipeActivity","got ${it.value}")
                    var user: User? = null
                    if(it.value != null){
                        val map = it.value as HashMap<*,*>
                        user = User(map.get("uid").toString(),map.get("username").toString(),map.get("profileImageUrl").toString())
                    }
                    if (user?.uid == FirebaseAuth.getInstance().uid) {
                        Log.d("SelectedFoodActivity","removing ref, ${it.key}")
                        FirebaseDatabase.getInstance().getReference("/foods/${food?.fid}/users/${it.key}").removeValue()
                    }
                }
                ref1.push().setValue(currentUser)
            }
        })

    }

}

private class UserGroupItem(val uid: String, val name: String){
    constructor() : this("", "")
}