package com.example.mynoteapp.model

import android.telecom.Call

data class Note(
    var title: String,
    var tasks: ArrayList<Task>,
    var isComplete: Boolean = false,
    val date: String
)