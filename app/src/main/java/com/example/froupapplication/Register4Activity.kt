package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Register4Activity: AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register4)

//        register_ButtonNext.setOnClickListener{
//            val intent = Intent(this, Register5Activity::class.java)
//            startActivity(intent)
//        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        val listView = findViewById<ListView>(R.id.list_view)

        val listName = arrayOf("Indian", "SouthAsian", "Chinese", "Pizza", "Italian", "International", "Thai", "Irish", "Asian Fusion",
        "Alcohol", "American", "BBQ", "Burgers", "Gluten Free", "Convenience", "Curry", "Danish", "Dessert", "Greek", "Halal", "Healthy choices", "Kebab")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, resources.getStringArray(R.array.foodPreferences))
        listView.adapter = arrayAdapter
        listView.onItemClickListener  = this
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (listName.contains(query)) {
                    arrayAdapter.filter.filter(query)
                } else {
                    Toast.makeText(this@Register4Activity, "No Match Found", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return false
            }


        })



        val Next: Button = findViewById(R.id.NextButtonF)
        Next.setOnClickListener {
            val intent = Intent(this, Register5Activity::class.java)
            startActivity(intent)
        }

        val Back: Button = findViewById(R.id.Back)
        Back.setOnClickListener {
            val intent = Intent(this, Register3Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: String = parent?.getItemAtPosition(position) as String
        Toast.makeText(applicationContext,
        "foodPreferences : $items",
        Toast.LENGTH_LONG).show()
    }
}








