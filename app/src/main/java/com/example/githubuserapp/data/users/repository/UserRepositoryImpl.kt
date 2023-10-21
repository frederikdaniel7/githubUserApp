package com.example.githubuserapp.data.users.repository

import com.example.githubuserapp.data.common.utils.WrappedListResponse
import com.example.githubuserapp.data.common.utils.WrappedResponse
import com.example.githubuserapp.data.users.remote.api.UsersApi
import com.example.githubuserapp.data.users.remote.dto.UserDetailResponse
import com.example.githubuserapp.data.users.remote.dto.UserListResponse
import com.example.githubuserapp.presentation.common.base.BaseResult
import com.example.githubuserapp.domain.users.UserRepository
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.domain.users.entity.UserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val usersApi: UsersApi) : UserRepository {
    override suspend fun getUserList(): Flow<BaseResult<List<UserEntity>, WrappedListResponse<UserListResponse>>> {
        return flow {
            val response = usersApi.getUserList()
            if (response.isSuccessful){
                val userResponses = response.body() ?: emptyList()
                val users = userResponses.map { userResponse ->
                    UserEntity(
                        userResponse.login,
                        userResponse.avatarUrl,
                        userResponse.id
                    )
                }
                emit(BaseResult.Success(users))
            }else{
                val type = object : TypeToken<WrappedListResponse<UserListResponse>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<UserListResponse>>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getUserDetail(username: String): Flow<BaseResult<UserDetailEntity, WrappedResponse<UserDetailResponse>>> {
        return flow {
            val response = usersApi.getUserDetail(username)
            if(response.isSuccessful){
                val body = response.body()
                val user = UserDetailEntity(
                    body!!.login,
                    body.name,
                    body.followingUrl,
                    body.followersUrl,
                    body.followers,
                    body.following,
                    body.publicRepos,
                    body.avatarUrl,
                    body.company,
                    body.location,
                    body.id,
                    body.email
                    )
                emit(BaseResult.Success(user))
            }else{
                val type = object : TypeToken<WrappedResponse<UserDetailResponse>>(){}.type
                val err = Gson().fromJson<WrappedResponse<UserDetailResponse>>(response.errorBody()!!.charStream(), type)!!
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}