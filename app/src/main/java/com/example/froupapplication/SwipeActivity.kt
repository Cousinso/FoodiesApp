package com.example.froupapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView;
import android.view.View;
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import java.util.ArrayList
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_from_row.view.*

class SwipeActivity : AppCompatActivity() {

    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.froupapplication.R.layout.activity_swipe)
        val cardStackView = findViewById<CardStackView>(com.example.froupapplication.R.id.card_stack_view)
        manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    Toast.makeText(this@SwipeActivity, "Direction Right", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Top) {
                    Toast.makeText(this@SwipeActivity, "Direction Top", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Left) {
                    Toast.makeText(this@SwipeActivity, "Direction Left", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(this@SwipeActivity, "Direction Bottom", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(com.example.froupapplication.R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(com.example.froupapplication.R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }
        })
        manager!!.setStackFrom(StackFrom.None)
        manager!!.setVisibleCount(3)
        manager!!.setTranslationInterval(8.0f)
        manager!!.setScaleInterval(0.95f)
        manager!!.setSwipeThreshold(0.3f)
        manager!!.setMaxDegree(20.0f)
        manager!!.setDirections(Direction.FREEDOM)
        manager!!.setCanScrollHorizontal(true)
        manager!!.setSwipeableMethod(SwipeableMethod.Manual)
        manager!!.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun addList(): List<ItemModel> {
        val items: MutableList<ItemModel> = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {
                    Log.d("NewMessageActivity", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {

                        // Load user image into chat log
                        var uri = ""
                        FirebaseDatabase.getInstance().getReference("/users/${user.uid}").child("profileImageUrl").get().addOnSuccessListener {
                            Log.d("GroupChatLogActivity","Got ${it.value}")
                            if(it.value != null){
                                uri = it.value as String
                            }
                            val target = findViewById<ImageView>(com.example.froupapplication.R.id.swipeActivityImageView)

                            items.add(ItemModel( uri, user.username, user.Bio, user.food))
                        }
                    }
                }
            }
        })

        items.add(ItemModel("https://firebasestorage.googleapis.com/v0/b/test-538b3.appspot.com/o/images%2F63e058f7-4de2-473a-aa65-607597ceecef?alt=media&token=2c163b8c-7493-4106-bfbe-8e4cc221d9f9", "username", "user.Bio", "here"))
        return items
    }

    companion object {
        private const val TAG = "SwipeActivity"
    }
}
/*class SwipeActivity : AppCompatActivity() {

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
