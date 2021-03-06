package com.example.froupapplication

class ChatMessage(val id: String, val fromID: String, val toID: String, val timestamp: Long, val text: String){
    constructor() : this("", "", "", -1, "")
}