package com.example.androidproject.todo.data.remote

import com.example.androidproject.todo.data.Item
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemEvent(val type: String, val payload: Item)
