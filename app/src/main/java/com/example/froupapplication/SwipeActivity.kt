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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter

class SwipeActivity : AppCompatActivity() {

        private var al: ArrayList<String>? = null
        var users: ArrayList<User>? = null
        var correctUsers: ArrayList<User>? = null
        private var arrayAdapter: ArrayAdapter<String>? = null
        private var i = 0
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_swipe)

            val auth = FirebaseAuth.getInstance()
            val curUser = auth.currentUser
            val ref = FirebaseDatabase.getInstance().getReference("/users")
            val foodPref = intent.getParcelableExtra<Food>(FoodSelectionActivity.FOOD_KEY)


            ref.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dbu in snapshot.children){
                        val nextUser = dbu.getValue(User::class.java)
                        if (nextUser != null){
                            users?.add(nextUser)
                            //Log.d("SwipeActivity", "User ${nextUser.username} added to list")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            for (dbu in users!!){
                if(dbu.food == foodPref?.name)
                    correctUsers!!.add(dbu)
                    Log.d("SwipeActivity", "Correcr user: ${dbu.username}, ${dbu.food}")
            }


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

//        //val adapter = GroupAdapter<GroupieViewHolder>()
//        val cardStackView = findViewById<CardStackView>(R.id.card_stack_view)
//        cardStackView.layoutManager = CardStackLayoutManager(this)
//        //cardStackView.adapter = CardStackAdapter()


    //}
//}
/*
public class CardStackAdapter(items: List<UserItem>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>(){
    private var items: List<UserItem>


    @NonNull

    fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val view: View = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    fun getItemCount(): Int {
        return items.field
    }
    public inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image:
    }

}
*/
