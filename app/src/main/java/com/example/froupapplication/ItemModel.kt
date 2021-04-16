package com.example.froupapplication

import android.widget.ImageView
import com.squareup.picasso.RequestCreator

class ItemModel {
    var image: String? = null
        private set
    var nama: String? = null
        private set
    var usia: String? = null
        private set
    var kota: String? = null
        private set
    var uid: String? = null
        private set

    constructor() {}
    constructor(image: String, nama: String, usia: String?, kota: String?,uid: String?) {
        this.image = image
        this.nama = nama
        this.usia = usia
        this.kota = kota
        this.uid = uid
    }
}