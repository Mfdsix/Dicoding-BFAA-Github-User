package com.zgenit.githubuser.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zgenit.githubuser.R
import com.zgenit.githubuser.fragments.FollowerFragment
import com.zgenit.githubuser.fragments.FollowingFragment

class SectionPagerAdapter(private val context: Context, private val username: String, frameManager: FragmentManager): FragmentPagerAdapter(frameManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = when(position){
            0 -> FollowerFragment(username)
            1 -> FollowingFragment(username)
            else -> null
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

}