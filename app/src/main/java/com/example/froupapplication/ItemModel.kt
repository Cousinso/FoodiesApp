package com.example.froupapplication

class ItemModel {
    var image: String? = null
        private set
    var name: String? = null
        private set
    var age: String? = null
        private set
    var city: String? = null
        private set
    var uid: String? = null
        private set

    constructor() {}
    constructor(image: String, name: String, age: String?, city: String?, uid: String?) {
        this.image = image
        this.name = name
        this.age = age
        this.city = city
        this.uid = uid
    }
}