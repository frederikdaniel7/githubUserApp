package com.example.githubuserapp.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.domain.users.entity.UserEntity
import com.example.githubuserapp.presentation.common.base.BaseResult
import com.example.githubuserapp.domain.users.usecase.UserUseCase
import com.example.githubuserapp.presentation.common.extension.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private val state =
        MutableStateFlow<MainActivityState>(MainActivityState.Init)
    val mState: StateFlow<MainActivityState> get() = state

    private fun setLoading(isLoading: Boolean) {
        state.value = MainActivityState.IsLoading(isLoading)
    }
    private fun showToast(message: String) {
        state.value = MainActivityState.ShowToast(message)
    }

    private val users = MutableStateFlow<List<UserEntity>>(mutableListOf())
    val mUsers : StateFlow<List<UserEntity>>get() = users

    private val userDetail = MutableStateFlow<UserDetailEntity?>(null)
    val mUserDetail : StateFlow<UserDetailEntity?>get() = userDetail

    fun getUserList(){
        viewModelScope.launch {
            userUseCase.getUserList()
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    Log.w(TAG, "getUserList:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            users.value = result.data
                            Log.w(TAG, result.data.toString())
                        }
                        is BaseResult.Error -> {
                            showToast("Error")
                        }
                    }
                }
        }
    }


}

sealed class MainActivityState{
    object Init : MainActivityState()
    object SuccessCreate : MainActivityState()
    data class IsLoading(val isLoading: Boolean) : MainActivityState()
    data class ShowToast(val message: String) : MainActivityState()

}