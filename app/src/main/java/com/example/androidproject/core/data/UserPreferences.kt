package com.example.androidproject.core.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPreferences(val username: String = "", val token: String = "")
