package com.example.githubuserapp.data.users

import com.example.githubuserapp.data.common.module.NetworkModule
import com.example.githubuserapp.data.users.remote.api.UsersApi
import com.example.githubuserapp.data.users.repository.UserRepositoryImpl
import com.example.githubuserapp.domain.users.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserModule {

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit) : UsersApi{
        return retrofit.create(UsersApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(usersApi: UsersApi): UserRepository{
        return UserRepositoryImpl(usersApi)
    }

}