package com.example.githubuserapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.BuildConfig
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.domain.users.entity.UserEntity
import com.example.githubuserapp.infra.utils.SharedPrefs
import com.example.githubuserapp.presentation.detail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        sharedPrefs.saveToken(BuildConfig.API_TOKEN)
        showRecyclerView()
        viewModel.getUserList()
        observe()

    }

    private fun observe() {
        observeState()
        observeUsers()
    }

    private fun observeState(){
        viewModel.mState
            .flowWithLifecycle(this.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(this.lifecycleScope)
    }
    private fun observeUsers(){
        viewModel.mUsers
            .flowWithLifecycle(this.lifecycle, Lifecycle.State.STARTED)
            .onEach { users ->
                handleUsers(users)
            }
            .launchIn(this.lifecycleScope)
    }

    private fun handleUsers(users: List<UserEntity>){
        activityMainBinding.recyclerViewMain.adapter.let {
            if (it is MainUserAdapter){
                it.updateList(users)
            }
        }

    }



    private fun handleState(state: MainActivityState) {
        when (state) {
            is MainActivityState.IsLoading -> handleLoading(state.isLoading)
            is MainActivityState.ShowToast -> Toast.makeText(this,state.message, Toast.LENGTH_SHORT).show()
            is MainActivityState.Init -> Unit
            else -> {}
        }
    }
    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            activityMainBinding.mainProgressBar.visibility = View.VISIBLE

        } else {
            activityMainBinding.mainProgressBar.visibility = View.GONE
        }
    }
    private fun showRecyclerView(){
        val userAdapter = MainUserAdapter(mutableListOf())
        activityMainBinding.recyclerViewMain.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        userAdapter.setOnItemClickCallback(object : MainUserAdapter.OnItemClickCallback{
            override fun onItemClicked(userDetail: UserEntity) {
                showSelected(userDetail.login)
            }
        })
    }

    private fun showSelected(username: String){
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USERNAME, username)
        startActivity(intent)
    }
}