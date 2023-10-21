package com.example.githubuserapp.domain.users

import com.example.githubuserapp.data.common.utils.WrappedListResponse
import com.example.githubuserapp.data.common.utils.WrappedResponse
import com.example.githubuserapp.data.users.remote.dto.UserDetailResponse
import com.example.githubuserapp.data.users.remote.dto.UserListResponse
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.presentation.common.base.BaseResult
import com.example.githubuserapp.domain.users.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserList() : Flow<BaseResult<List<UserEntity>, WrappedListResponse<UserListResponse>>>
    suspend fun getUserDetail(username: String) : Flow<BaseResult<UserDetailEntity, WrappedResponse<UserDetailResponse>>>
}