package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.froupapplication.FoodSelectionActivity.Companion.FOOD_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter

class SwipeActivity : AppCompatActivity() {

        private var al: ArrayList<String>? = null
        var users: ArrayList<User>? = ArrayList()
        var correctUsers: ArrayList<User>? = null
        private var arrayAdapter: ArrayAdapter<String>? = null
        private var i = 0
        companion object {
        val SWIPE_KEY = "SwipeActivity"
    }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_swipe)
            val auth = FirebaseAuth.getInstance()
            val curUser = LatestMessagesActivity.currentUser
            //val fid = intent.getParcelableExtra<Food>(SelectedFoodActivity.FOOD_KEY).fid
            val ref = FirebaseDatabase.getInstance().getReference("/users")
           // val foodRef = FirebaseDatabase.getInstance().getReference("/foods/$fid")
            //Log.d("SwipeActivity", "foodPref =  $foodPref")
            //Log.d("SwipeActivity", "User food =  ${curUser?.food}")

            Log.d(SWIPE_KEY, "curUser food: ${curUser?.food}")

            ref.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var i = 0
                    for (dbu in snapshot.children){
                        val nextUser = dbu.getValue(User::class.java)
                        if (nextUser != null && nextUser.food == curUser?.food){
                            users?.add(nextUser)
                            Log.d("SwipeActivity", "User ${nextUser.username}, ${nextUser.food} added to list")
                            Log.d(SWIPE_KEY, "users[$i] user: ${users?.get(i)?.username}")
                            i++
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

//            if(!users.isNullOrEmpty()) {
//                for (i in 0 until users!!.size) {
//                    val nextUser = users!!.get(i)
//                    Log.d("SwipeActivity", "Iterating over list $i")
//                    Log.d(SWIPE_KEY, "nextUser:  ${nextUser.username}, ${nextUser.food}")
//                    if (nextUser.food == curUser!!.food) {
//                        correctUsers?.add(nextUser)
//                    }
//                }
//            }

//            for (dbu in users!!) {
//                Log.d(SWIPE_KEY, "${dbu.food}, ${curUser?.food}")
//                if (dbu.food == curUser?.food)
//                    correctUsers?.add(dbu)
//                Log.d("SwipeActivity", "Correct user: ${dbu.username}, ${dbu.food}")
//            }


            al = ArrayList()
            al!!.add("php")
            al!!.add("c")
            al!!.add("python")
            al!!.add("java")
            al!!.add("html")
            al!!.add("c++")
            al!!.add("css")
            al!!.add("javascript")
            al!!.add("Teri Maa Ka Bhosda")

            arrayAdapter = ArrayAdapter(this, R.layout.item, R.id.helloText, al!!)
            val flingContainer = findViewById<View>(R.id.frame) as SwipeFlingAdapterView
            flingContainer.adapter = arrayAdapter
            flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
                override fun removeFirstObjectInAdapter() {
                    // this is the simplest way to delete an object from the Adapter (/AdapterView)
                    Log.d("LIST", "removed object!")
                    al!!.removeAt(0)
                    arrayAdapter!!.notifyDataSetChanged()
                }

                override fun onLeftCardExit(dataObject: Any) {
                    Toast.makeText(this@SwipeActivity, "Left!!", Toast.LENGTH_SHORT).show()
                }

                override fun onRightCardExit(dataObject: Any) {
                    Toast.makeText(this@SwipeActivity, "Right !!", Toast.LENGTH_SHORT).show()
                }

                override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                    al!!.add("XML $i")
                    arrayAdapter!!.notifyDataSetChanged()
                    Log.d("LIST", "notified")
                    i++
                }

                override fun onScroll(scrollProgressPercent: Float) {}
            })

            flingContainer.setOnItemClickListener { itemPosition, dataObject ->
                Toast.makeText(this@SwipeActivity, "Right !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
