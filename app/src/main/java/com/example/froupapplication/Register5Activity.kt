package com.example.froupapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Register5Activity: AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register5)

//        register_ButtonNext.setOnClickListener{
//            val intent = Intent(this, Register5Activity::class.java)
//            startActivity(intent)
//        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        val listView = findViewById<ListView>(R.id.list_view)

        val listName = arrayOf("Milk", "Peanut", "Egg", "Wheat", "Soy", "Fin fish", "Fish", "Tree nut","Chicken", "Beef", "Shell Fish", "Cheese")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listName)
        listView.adapter = arrayAdapter
        listView.onItemClickListener  = this
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (listName.contains(query)) {
                    arrayAdapter.filter.filter(query)
                } else {
                    Toast.makeText(this@Register5Activity, "No Match Found", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return false
            }

        })
        val Next2: Button = findViewById(R.id.NextButtonF)
        Next2.setOnClickListener {
            val intent = Intent(this, FoodSelectionActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Registration done", Toast.LENGTH_SHORT).show()
        }

        val Back2: Button = findViewById(R.id.Back)
        Back2.setOnClickListener {
            val intent = Intent(this, Register4Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: String = parent?.getItemAtPosition(position) as String
        Toast.makeText(applicationContext,
                "Allergies/Intolerances : $items",
                Toast.LENGTH_LONG).show()
    }
}



