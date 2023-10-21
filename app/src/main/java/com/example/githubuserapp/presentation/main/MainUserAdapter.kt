package com.example.githubuserapp.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.UserItemBinding
import com.example.githubuserapp.domain.users.entity.UserEntity

class MainUserAdapter(private val users : MutableList<UserEntity>): RecyclerView.Adapter<MainUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainUserAdapter.ViewHolder {
        val viewBinding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainUserAdapter.ViewHolder(viewBinding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemCount(): Int {
     return users.size
    }

    fun updateList(mUsers: List<UserEntity>){
        users.clear()
        users.addAll(mUsers)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserEntity)
    }

    override fun onBindViewHolder(holder: MainUserAdapter.ViewHolder, position: Int) {
        val username = users[position].login
        val avatar = users[position].avatar_url
        Glide.with(holder.itemView.context).load(avatar).circleCrop().into(holder.binding.imgUser)
        holder.binding.textViewUsername.text = username
        holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(users[position])
        }
    }
    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)
}