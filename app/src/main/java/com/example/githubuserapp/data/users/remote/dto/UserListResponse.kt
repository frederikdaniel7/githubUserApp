package com.example.githubuserapp.data.users.remote.dto

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("id")
    val id: Int
)
