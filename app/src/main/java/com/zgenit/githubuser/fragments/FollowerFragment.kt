package com.zgenit.githubuser.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgenit.githubuser.ProfileActivity
import com.zgenit.githubuser.R
import com.zgenit.githubuser.adapters.UserAdapter
import com.zgenit.githubuser.models.UserItems
import com.zgenit.githubuser.view_models.FollowerFragmentModel
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.include_notification.*

class FollowerFragment(username: String) : Fragment() {

    private var username: String?= ""
    private lateinit var followerFragmentModel: FollowerFragmentModel
    private lateinit var adapter: UserAdapter

    init{
        this.username = username
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setViewVisibilities(0)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                showUserProfile(data)
            }
        })
        rv_followers.layoutManager = LinearLayoutManager(context)
        rv_followers.adapter = adapter

        followerFragmentModel = ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory()).get(FollowerFragmentModel::class.java)
        followerFragmentModel.setFollowers(username!!)
        followerFragmentModel.getFollowers().observe(activity!!, Observer {
            if(it.size != 0){
                adapter.setData(it)
                setViewVisibilities(1)
            }else{
                notification_text.setText(R.string.no_user_found)
                setViewVisibilities(2)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setViewVisibilities(state: Int){
        when(state){
            1 -> {
                // showing recyclerview
                progressBar.visibility = View.GONE
                rv_followers.visibility = View.VISIBLE
                notification_wrap.visibility = View.GONE
            }
            2 -> {
                // showing notification view
                progressBar.visibility = View.GONE
                rv_followers.visibility = View.GONE
                notification_wrap.visibility = View.VISIBLE
            }
            else -> {
                // showing loading
                progressBar.visibility = View.VISIBLE
                rv_followers.visibility = View.GONE
                notification_wrap.visibility = View.GONE
            }
        }
    }

    private fun showUserProfile(data: UserItems) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("username", data.username)
        startActivity(intent)
    }
}