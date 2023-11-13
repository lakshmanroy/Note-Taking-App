package com.lkrd.firebases

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lkrd.firebases.databinding.ActivityUpdateUserBinding
import com.lkrd.firebases.databinding.UserItemBinding

class UserAdapter(var context:Context,
                 val userList:ArrayList<Users>):RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {

    inner class UsersViewHolder(val adapterBinding:UserItemBinding) : RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding=UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
       holder.adapterBinding.textViewName.text=userList[position].userName
        holder.adapterBinding.textViewAge.text=userList[position].userAge.toString()
        holder.adapterBinding.textViewEmail.text=userList[position].userEmail

        holder.adapterBinding.linearLayout.setOnClickListener {

            val intent=Intent(context,UpdateUserActivity::class.java)
            intent.putExtra("id",userList[position].userId)
            intent.putExtra("name",userList[position].userName)
            intent.putExtra("age",userList[position].userAge)
            intent.putExtra("email",userList[position].userEmail)
            context.startActivity(intent)
        }
    }
    fun getUserId(position:Int): String{
        return userList[position].userId
    }

}