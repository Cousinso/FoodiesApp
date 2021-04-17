package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_group_vs_pair.*

class GroupVsPairActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_vs_pair)

        val food = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY)
        val fid = food?.fid

        val currentUser = LatestMessagesActivity.currentUser
        val uid = currentUser?.uid
        val username = currentUser?.username

        pairButton.setOnClickListener {
            val intent = Intent(it.context, SwipeActivity::class.java)
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
}

private class UserGroupItem(val uid: String, val name: String){
    constructor() : this("", "")
}