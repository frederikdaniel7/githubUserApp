package com.example.githubuserapp.data.users.remote.api

import com.example.githubuserapp.data.common.utils.WrappedListResponse
import com.example.githubuserapp.data.users.remote.dto.UserDetailResponse
import com.example.githubuserapp.data.users.remote.dto.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UsersApi {
    @GET("users")
    suspend fun getUserList(): Response<List<UserListResponse>>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path ("username") username : String) : Response <UserDetailResponse>
}