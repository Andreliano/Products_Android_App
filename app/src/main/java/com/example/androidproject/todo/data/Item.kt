package com.example.androidproject.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.Date

@Entity(tableName = "items")
@JsonClass(generateAdapter = true)
data class Item(
    @PrimaryKey val _id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val amount: Int = 0,
    val category: String = "",
    val isAvailable: Boolean = false,
    var isSaved: Boolean = true,
    val producer: String = "",
    val specifications: String = "",
    val additionDate: Date = Date(System.currentTimeMillis()),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
