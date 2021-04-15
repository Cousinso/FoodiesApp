package com.example.froupapplication

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
import com.lorentzos.flingswipe.SwipeFlingAdapterView

class SwipeActivity : AppCompatActivity() {

        private var al: ArrayList<String>? = null
        private var arrayAdapter: ArrayAdapter<String>? = null
        private var i = 0
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_swipe)
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
                    //Do something on the left!
                    //You also have access to the original object.
                    //If you want to use it just cast it (String) dataObject
                    Toast.makeText(this@SwipeActivity, "Left!!", Toast.LENGTH_SHORT).show()
                }

                override fun onRightCardExit(dataObject: Any) {
                    Toast.makeText(this@SwipeActivity, "Right !!", Toast.LENGTH_SHORT).show()
                }

                override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                    // Ask for more data here
                    al!!.add("XML $i")
                    arrayAdapter!!.notifyDataSetChanged()
                    Log.d("LIST", "notified")
                    i++
                }

                override fun onScroll(scrollProgressPercent: Float) {}
            })


            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener { itemPosition, dataObject -> Toast.makeText(this@SwipeActivity, "Right !!", Toast.LENGTH_SHORT).show() }
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
