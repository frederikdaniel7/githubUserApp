package com.example.githubuserapp.presentation.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.databinding.ActivityUserDetailBinding
import com.example.githubuserapp.domain.users.entity.UserDetailEntity
import com.example.githubuserapp.presentation.detail.ui.theme.GithubUserAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserDetailActivity : ComponentActivity() {
    private lateinit var activityDetailBinding: ActivityUserDetailBinding
    private lateinit var username: String
    private val viewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        viewModel.getUserDetail(username)
        observe()
    }

    private fun observe(){
        observeState()
        observeProduct()
    }
    private fun observeState(){
        viewModel.mState.flowWithLifecycle(this.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(this.lifecycleScope)
    }

    private fun observeProduct(){
        viewModel.mUserDetail.flowWithLifecycle(this.lifecycle, Lifecycle.State.STARTED)
            .onEach { users ->
                if (users !== null) {
                    handleUserDetail(users)

                }
            }
            .launchIn(this.lifecycleScope)
    }
    private fun handleUserDetail(users: UserDetailEntity) {
        activityDetailBinding.imageView.let {
            Glide.with(this)
                .load(users.avatarUrl)
                .centerCrop()
                .into(it) }
        activityDetailBinding.apply {
            idTVusername.text = users.login
            idTVname.text = users.name
            idTVfollowernum.text = users.followers.toString()
            idTVfollowingnum.text = users.following.toString()
            idTVreponum.text = users.publicRepos.toString()
            idTVcompany.text = users.company
            idTVlocation.text = users.location
        }

    }

    private fun handleState(state: UserDetailActivityState){
        when(state){
            is UserDetailActivityState.IsLoading -> handleLoading(state.isLoading)
            is UserDetailActivityState.ShowToast -> Toast.makeText(this,state.message, Toast.LENGTH_SHORT).show()
            is UserDetailActivityState.Init -> Unit
            UserDetailActivityState.SuccessCreate -> TODO()
        }
    }
    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            activityDetailBinding.progressBar.visibility = View.VISIBLE

        } else {
            activityDetailBinding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        var EXTRA_USERNAME = "id"
    }
}

