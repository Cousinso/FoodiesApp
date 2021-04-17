package com.example.froupapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView;
import android.view.View;
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import java.util.ArrayList
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_from_row.view.*
import androidx.annotation.NonNull
import androidx.core.view.get
import com.example.froupapplication.FoodSelectionActivity.Companion.FOOD_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_chat_log.*

class SwipeActivity : AppCompatActivity() {

    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    val curUser = LatestMessagesActivity.currentUser
    var fid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.froupapplication.R.layout.activity_swipe)
        val cardStackView = findViewById<CardStackView>(com.example.froupapplication.R.id.card_stack_view)
        //getCurrentSwipes()
        fid = intent.getStringExtra("foodid")!!
        manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                //Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)

            }

            var profileUid = ""


            override fun onCardSwiped(direction: Direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    if(profileUid != ""){
                        checkOtherUserSwipe(profileUid)
                        addToSwipeRight(profileUid)
                    }
                    //Toast.makeText(this@SwipeActivity, "Direction Right", Toast.LENGTH_SHORT).show()
                }
                /*if (direction == Direction.Top) {
                    TODO()
                    //Toast.makeText(this@SwipeActivity, "Direction Top", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Left) {
                    TODO()
                    //Toast.makeText(this@SwipeActivity, "Direction Left", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Bottom) {
                    TODO()
                    //Toast.makeText(this@SwipeActivity, "Direction Bottom", Toast.LENGTH_SHORT).show()
                }*/
            }


            override fun onCardRewound() {
                //Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardCanceled() {
                //Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                //val tv = view.findViewById<TextView>(com.example.froupapplication.R.id.item_name)
                //val tv2 = view.findViewById<TextView>(com.example.froupapplication.R.id.item_uid)
                //Log.d(
                  //  TAG,
                    //"onCardAppeared: " + position + ", nama: " + tv.text + ", id: " + tv2.text
                //)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                profileUid =
                    view.findViewById<TextView>(com.example.froupapplication.R.id.item_uid).text.toString()
                //val tv = view.findViewById<TextView>(com.example.froupapplication.R.id.item_name)
                //Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
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

    private fun checkOtherUserSwipe(uid: String) {
        var bool = false

        FirebaseDatabase.getInstance().getReference("/users/$uid/right-swiped").get()

                .addOnSuccessListener {
                    //Log.d("SwipeActivity","Got ${it.value} as other user right swipes")
                    it.children.forEach() {
                        val ituid = it.value.toString()
                        //Log.d(TAG, "curuid value: ${curUser!!.uid}, ituid value: $ituid")
                        if (ituid == curUser!!.uid) {
                            Log.d(TAG, "Equal")
                            createMessage(uid)
                            bool = true
                            val intent = Intent(this, MessageOrSwipeActivity::class.java)
                            intent.putExtra("User",uid)
                            startActivity(intent)
                        }
                    }


                    return@addOnSuccessListener
                }


    }

    private fun addToSwipeRight(uid: String){
        val userRef =  FirebaseDatabase.getInstance().getReference("/users/${curUser!!.uid}/right-swiped")
        /*userRef.setValue(uid)
            .addOnSuccessListener {
                Log.d("SwipeActivity", "Saved right swipe: $uid")
                //chatLogRecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }*/

        val ref1 = FirebaseDatabase.getInstance().getReference("/users/${curUser!!.uid}/right-swiped").push()



        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("SwipeActivity","snapshot: $snapshot")
                snapshot.children.forEach {
                    //Log.d("NewMessageActivity", it.toString())
                    Log.d("SwipeActivity","got ${it.value}")
                    //var user: User? = null
                    if(it.value != null) {
                        val newuid = it.value as String

                        Log.d("SwipeActivity", "uid found: ${newuid}, uid checking: $uid")
                        if (newuid == uid) {
                            Log.d("SwipeActivity", "removing ref, ${it.key}")
                            FirebaseDatabase.getInstance()
                                .getReference("/users/${curUser.uid}/right-swiped/${it.key}")
                                .removeValue()
                        }
                    }
                }
                Log.d("SwipeActivity", "end of checks for /users/rightswiped")
                ref1.setValue(uid)
            }
        })
    }

    private fun createMessage(uid: String){
        Log.d(TAG,"Both swiped right, starting chat with uid: $uid")
        val text = "You both swiped right, start chatting!"

        val fromID = FirebaseAuth.getInstance().uid
        val toID = uid

        val reference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID/$toID")
        val toReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/$fromID")

        if (fromID == null) return
        if (toID == null) return

        val chatMessage = ChatMessage(reference.key!!, fromID, toID, System.currentTimeMillis() / 1000, text)

        reference.setValue(chatMessage)
        toReference.setValue(chatMessage)
        Log.d("SwipeActivity","Two users swiped right, started new message")
    }



    private fun addList(): List<ItemModel> {
        val items: MutableList<ItemModel> = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("/foods/$fid/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {
                    //Log.d("NewMessageActivity", it.toString())
                    //Log.d("SwipeActivity","got ${it.value}")
                    var user: User? = null
                    if(it.value != null ){
                        val map = it.value as HashMap<*,*>
                        user = User(map.get("uid").toString(),map.get("username").toString(),map.get("profileImageUrl").toString())
                    }
                    if (user != null && user.uid != curUser!!.uid && user.profileImageUrl != "") {
                        // Load user image into chat log
                        items.add(ItemModel( user.profileImageUrl, user.username,"","",user.uid))
                    }
                }
            }
        })

        items.add(ItemModel("https://previews.123rf.com/images/martialred/martialred1910/martialred191000006/131619575-hand-with-finger-swiping-or-swipe-left-and-right-gesture-line-art-vector-icon-for-apps-and-websites.jpg", "", "", "",""))
        return items
    }

    companion object {
        private const val TAG = "SwipeActivity"
    }
}
