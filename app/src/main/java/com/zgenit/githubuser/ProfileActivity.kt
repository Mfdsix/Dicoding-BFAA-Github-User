package com.zgenit.githubuser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zgenit.githubuser.adapters.SectionPagerAdapter
import com.zgenit.githubuser.models.UserItems
import com.zgenit.githubuser.models.UserProfile
import com.zgenit.githubuser.view_models.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.include_notification.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private var userProfile: UserProfile ?= null
    private var user: UserItems = UserItems()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.elevation = 0f
        supportActionBar?.setTitle(R.string.user_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewVisibilities(0)
        val bundle: Bundle? = intent.extras
        if(bundle != null){
            user = bundle.getParcelable("user")!!
        }

        profileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel::class.java)
        profileViewModel.setUsers(user.username!!)
        profileViewModel.getUsers().observe(this, Observer {
            if(it != null){
                userProfile = it
                setViewVisibilities(1)
                setUserProfile()
            }else{
                notification_text.setText(R.string.no_user_found)
                setViewVisibilities(2)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setUserProfile(){
        Glide.with(this)
            .load(userProfile!!.avatar)
            .placeholder(R.color.colorGrey)
            .into(img_user)

        txt_user_name.text = userProfile?.username
        txt_user_fullname.text = userProfile?.fullname
        txt_followers_amount.text = resources.getString(R.string.user_followers_amount, userProfile!!.followers)
        txt_followings_amount.text = resources.getString(R.string.user_following_amount, userProfile!!.following)
        txt_repositories_amount.text = resources.getString(R.string.user_repository_amount, userProfile!!.publicRepos)
        txt_user_office.text = userProfile?.office
        txt_user_location.text = userProfile?.location

        setTabLayout()
    }

    private fun setTabLayout(){
        val sectionPagerAdapter = SectionPagerAdapter(this, userProfile?.username!!, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun setViewVisibilities(state: Int){
        when(state){
            1 -> {
                // showing main content
                progressBar.visibility = View.GONE
                main_wrap.visibility = View.VISIBLE
                notification_wrap.visibility = View.GONE
            }
            2 -> {
                // showing notification view
                progressBar.visibility = View.GONE
                main_wrap.visibility = View.GONE
                notification_wrap.visibility = View.VISIBLE
            }
            else -> {
                // showing loading
                progressBar.visibility = View.VISIBLE
                main_wrap.visibility = View.GONE
                notification_wrap.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}