package com.zgenit.githubuser.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zgenit.githubuser.models.UserItems
import com.zgenit.githubuser.models.UserProfile
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ProfileViewModel : ViewModel() {

    companion object {
        const val GITHUB_API_URL = "https://api.github.com/users/"
    }

    val userProfile = MutableLiveData<UserProfile>()

    fun setUsers(query: String) {
        var userData: UserProfile? = null
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

                    userData = UserProfile()
                    userData!!.id = responseObject.getInt("id")
                    userData!!.nodeId = responseObject.getString("node_id")
                    userData!!.avatar = responseObject.getString("avatar_url")
                    userData!!.username = responseObject.getString("login")
                    userData!!.fullname = responseObject.getString("name")
                    userData!!.publicRepos = responseObject.getInt("public_repos")
                    userData!!.followers = responseObject.getInt("followers")
                    userData!!.following = responseObject.getInt("following")
                    userProfile.postValue(userData)

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

    fun getUsers(): LiveData<UserProfile> {
        return userProfile
    }
}