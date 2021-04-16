package com.example.froupapplication

import com.squareup.picasso.RequestCreator

class ItemModel {
    var image = 0
        private set
    var nama: String? = null
        private set
    var usia: String? = null
        private set
    var kota: String? = null
        private set

    constructor() {}
    constructor(image: Int, nama: String, usia: String?, kota: String?) {
        this.image = image
        this.nama = nama
        this.usia = usia
        this.kota = kota
    }
}