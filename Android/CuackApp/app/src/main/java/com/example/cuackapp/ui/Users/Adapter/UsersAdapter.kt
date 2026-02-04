package com.example.cuackapp.ui.Users.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cuackapp.R
import com.example.cuackapp.model.userModel.User


class usersAdapter(
    private var users: MutableList<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<usersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvRole: TextView = view.findViewById(R.id.tvRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvUsername.text = user.username
        holder.tvRole.text = user.type.name
        holder.ivAvatar.setImageResource(R.drawable.cuack_logo)


        holder.itemView.setOnClickListener { onUserClick(user) }
    }

    override fun getItemCount() = users.size


    fun updateList(newUsers: List<User>) {
        this.users.clear()
        this.users.addAll(newUsers)
        notifyDataSetChanged()
    }
}