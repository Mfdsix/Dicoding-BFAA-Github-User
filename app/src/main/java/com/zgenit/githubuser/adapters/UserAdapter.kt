package com.zgenit.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zgenit.githubuser.R
import com.zgenit.githubuser.models.UserItems
import kotlinx.android.synthetic.main.user_items.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    private val mData = ArrayList<UserItems>()

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mData[position]

        holder.username.text = user.username
        holder.userId.text = user.id.toString()
        holder.userNode.text = user.nodeId
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .into(holder.avatar)

        holder.wrap.setOnClickListener {  }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val wrap = view.user_wrap
        val username = view.txt_user_name
        val avatar = view.img_user
        val userId = view.txt_user_id
        val userNode = view.txt_user_node
    }

}