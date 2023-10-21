package com.example.githubuserapp.domain.users.entity

import com.google.gson.annotations.SerializedName

data class UserDetailEntity(
    val login: String,
    val name: String?,
    val followingUrl: String,
    val followersUrl: String,
    val followers: Int,
    val following: Int,
    val publicRepos: Int?,
    val avatarUrl: String?,
    val company: String?,
    val location: String?,
    val id: Int,
    val email: Any?
)
