package com.example.githubuserapp.domain.users.usecase

import com.example.githubuserapp.data.common.utils.WrappedListResponse
import com.example.githubuserapp.data.common.utils.WrappedResponse
import com.example.githubuserapp.data.users.remote.dto.UserDetailResponse
import com.example.githubuserapp.data.users.remote.dto.UserListResponse
import com.example.githubuserapp.presentation.common.base.BaseResult
import com.example.githubuserapp.domain.users.UserRepository
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.domain.users.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun getUserList() : Flow<BaseResult<List<UserEntity>, WrappedListResponse<UserListResponse>>>{
        return userRepository.getUserList()
    }

    suspend fun getUserDetail(username: String) : Flow <BaseResult<UserDetailEntity, WrappedResponse<UserDetailResponse>>>{
        return userRepository.getUserDetail(username)
    }

}