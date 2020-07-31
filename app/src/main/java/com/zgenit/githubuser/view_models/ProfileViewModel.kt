package com.zgenit.githubuser.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zgenit.githubuser.models.UserProfile
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ProfileViewModel : ViewModel() {

    companion object {
        const val GITHUB_API_URL = "https://api.github.com/users/"
    }

    val userProfile = MutableLiveData<UserProfile>()

    fun setUsers(query: String) {
        var userData: UserProfile
        val url = "${GITHUB_API_URL}${query}"

        val client = AsyncHttpClient()
        client.addHeader("Accept", "application/vnd.github.v3+json")
        client.addHeader("Authorization", "b84c50b376ec691500f507663cb5dedf3e841656")
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

                    userData = UserProfile(
                        responseObject.getInt("id"),
                        responseObject.getString("node_id"),
                        responseObject.getString("login"),
                        responseObject.getString("name"),
                        responseObject.getString("avatar_url"),
                        responseObject.getInt("public_repos"),
                        responseObject.getInt("followers"),
                        responseObject.getInt("following"),
                        responseObject.getString("company"),
                        responseObject.getString("location")
                    )
                    userProfile.postValue(userData)

                } catch (e: Exception) {
                    userProfile.postValue(null)
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                userProfile.postValue(null)
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUsers(): LiveData<UserProfile> {
        return userProfile
    }
}