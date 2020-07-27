package com.zgenit.githubuser

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgenit.githubuser.adapters.UserAdapter
import com.zgenit.githubuser.view_models.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_notification.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

        notification_text.setText(R.string.search_github_user)
        setViewVisibilities(2)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getUsers().observe(this, Observer {
            if(it.size != 0){
                adapter.setData(it)
                setViewVisibilities(1)
            }else{
                notification_text.setText(R.string.no_user_found)
                setViewVisibilities(2)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_github_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchGithubUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    private fun searchGithubUser(s: String) {
        if(s != ""){
            setViewVisibilities(0)
            mainViewModel.setUsers(s)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_setting -> {
                true
            }
            else -> true
        }
    }

    private fun setViewVisibilities(state: Int){
        when(state){
            1 -> {
                // showing recyclerview
                progressBar.visibility = View.GONE
                rv_users.visibility = View.VISIBLE
                notification_wrap.visibility = View.GONE
            }
            2 -> {
                // showing notification view
                progressBar.visibility = View.GONE
                rv_users.visibility = View.GONE
                notification_wrap.visibility = View.VISIBLE
            }
            else -> {
                // showing loading
                progressBar.visibility = View.VISIBLE
                rv_users.visibility = View.GONE
                notification_wrap.visibility = View.GONE
            }
        }
    }
}