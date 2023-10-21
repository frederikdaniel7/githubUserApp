package com.example.githubuserapp.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.domain.users.entity.UserEntity
import com.example.githubuserapp.domain.users.usecase.UserUseCase
import com.example.githubuserapp.presentation.common.base.BaseResult
import com.example.githubuserapp.presentation.common.extension.TAG
import com.example.githubuserapp.presentation.main.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel(){
    private val state =
        MutableStateFlow<UserDetailActivityState>(UserDetailActivityState.Init)
    val mState: StateFlow<UserDetailActivityState> get() = state

    private fun setLoading(isLoading: Boolean) {
        state.value = UserDetailActivityState.IsLoading(isLoading)
    }
    private fun showToast(message: String) {
        state.value = UserDetailActivityState.ShowToast(message)
    }

    private val userDetail = MutableStateFlow<UserDetailEntity?>(null)
    val mUserDetail : StateFlow<UserDetailEntity?> get() = userDetail


    fun getUserDetail(username: String){
        viewModelScope.launch {
            userUseCase.getUserDetail(username)
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    Log.w(TAG, "getUserDetail:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            userDetail.value = result.data
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
sealed class UserDetailActivityState{
    object Init : UserDetailActivityState()
    object SuccessCreate : UserDetailActivityState()
    data class IsLoading(val isLoading: Boolean) : UserDetailActivityState()
    data class ShowToast(val message: String) : UserDetailActivityState()

}