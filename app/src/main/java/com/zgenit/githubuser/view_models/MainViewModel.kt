package com.zgenit.githubuser.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zgenit.githubuser.models.UserItems
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    companion object{
        const val GITHUB_API_URL = "https://api.github.com/search/users?q="
    }

    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUsers(query: String) {
        val listItems = ArrayList<UserItems>()
        val url = "${GITHUB_API_URL}${query}"

        val client = AsyncHttpClient()
        client.addHeader("Accept", "application/vnd.github.v3+json")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItem = UserItems()
                        userItem.id = user.getInt("id")
                        userItem.nodeId = user.getString("node_id")
                        userItem.username = user.getString("login")
                        userItem.avatar = user.getString("avatar_url")

                        listItems.add(userItem)
                    }

                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                println(statusCode)
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUsers() : LiveData<ArrayList<UserItems>>{
        return listUsers
    }
}