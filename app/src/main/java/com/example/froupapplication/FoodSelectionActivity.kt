package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.telephony.mbms.MbmsErrors
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_food_selection.*
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.food_choice_1.*
import kotlinx.android.synthetic.main.food_choice_1.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class FoodSelectionActivity : AppCompatActivity() {
    companion object {
        val TAG = "FoodSelection"
        val currentUser: User? = null
        val FOOD_KEY = "FOOD_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_selection)

        getCurrentUser()
        verifyLogin()

        supportActionBar?.title = "Food Selection"

        recyclerViewFoodSelection.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        fetchFood()
    }

    private fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                LatestMessagesActivity.currentUser = snapshot.getValue(User::class.java)
                Log.d("FoodSelectionActivity", "Current user: ${LatestMessagesActivity.currentUser?.username}")
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun verifyLogin() {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.uid

        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchFood() {
        val ref = FirebaseDatabase.getInstance().getReference("/foods")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    Log.d("FoodSelectionActivity", it.toString())
                    val food = it.getValue(Food::class.java)
                    if (food != null) {
                        adapter.add(FoodItem(food))
                    }
                }

                // Sends user to chat logs with a person
                adapter.setOnItemClickListener { item, view ->
                    val foodItem = item as FoodItem
                    val intent = Intent(view.context, SelectedFoodActivity::class.java)
                    intent.putExtra(FOOD_KEY, foodItem.food)
                    startActivity(intent)
                }

                recyclerViewFoodSelection.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.food_latest_messages -> {
                val intent = Intent(this, LatestMessagesActivity::class.java)
                startActivity(intent)
            }
            R.id.food_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.food_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

class FoodItem(val food: Food): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.food_choice_1
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (food.foodImageUrl != "") {
            Picasso.get().load(food.foodImageUrl).into(viewHolder.itemView.foodImageView1)
        }
        viewHolder.itemView.foodNameTextViewFoodSelection.text = food.name
//        viewHolder.itemView.yesButtonFoodChoice1.setOnClickListener {
//            Log.d("FoodSelectionActivity", it.toString())
//            val intent = Intent(it.context, SelectedFoodActivity::class.java)
//        }
    }
}

@Parcelize
class Food(val fid: String, val name: String, val foodImageUrl: String) : Parcelable {
    // No-argument constructor
    constructor() : this("", "", "")
}