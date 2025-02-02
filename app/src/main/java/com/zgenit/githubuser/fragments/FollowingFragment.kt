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
import com.zgenit.githubuser.view_models.FollowingFragmentModel
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.include_notification.*

class FollowingFragment : Fragment() {

    private var username: String?= ""
    private lateinit var followingFragmentModel: FollowingFragmentModel
    private lateinit var adapter: UserAdapter

    companion object{
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setViewVisibilities(0)
        username = arguments?.getString(ARG_USERNAME)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                showUserProfile(data)
            }
        })
        rv_followings.layoutManager = LinearLayoutManager(context)
        rv_followings.adapter = adapter

        followingFragmentModel = ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory()).get(FollowingFragmentModel::class.java)
        followingFragmentModel.setFollowing(username!!)
        followingFragmentModel.getFollowing().observe(activity!!, Observer {
            if(it.size != 0){
                adapter.setData(it)
                setViewVisibilities(1)
            }else{
                notification_text.setText(R.string.no_following)
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
                rv_followings.visibility = View.VISIBLE
                notification_wrap.visibility = View.GONE
            }
            2 -> {
                // showing notification view
                progressBar.visibility = View.GONE
                rv_followings.visibility = View.GONE
                notification_wrap.visibility = View.VISIBLE
            }
            else -> {
                // showing loading
                progressBar.visibility = View.VISIBLE
                rv_followings.visibility = View.GONE
                notification_wrap.visibility = View.GONE
            }
        }
    }

    private fun showUserProfile(data: UserItems) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("user", data)
        startActivity(intent)
    }
}