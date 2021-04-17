package com.example.froupapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class CardStackAdapter(var items: List<ItemModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.item_image)
        var nama: TextView
        var usia: TextView
        var kota: TextView
        var uid: TextView
        fun setData(data: ItemModel) {
            //Log.d("SwipeActivity","image link ${data.image}, all data: $data")

            Picasso.get()
                    .load(data.image)
                    .fit()
                    .centerCrop()
                    .into(image)
            nama.text = data.name.toString()
            usia.text = data.age
            kota.text = data.city
            uid.text = data.uid
        }

        init {
            nama = itemView.findViewById(R.id.item_name)
            usia = itemView.findViewById(R.id.item_age)
            kota = itemView.findViewById(R.id.item_city)
            uid = itemView.findViewById(R.id.item_uid)
        }
    }

}
