package com.zgenit.githubuser.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zgenit.githubuser.models.UserItems
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerFragmentModel : ViewModel() {

    companion object {
        const val GITHUB_API_URL = "https://api.github.com/users/"
    }
    val listFollowers = MutableLiveData<ArrayList<UserItems>>()

    fun setFollowers(query: String) {
        val listItems = ArrayList<UserItems>()
        val url = "${GITHUB_API_URL}${query}/followers"

        val client = AsyncHttpClient()
        client.addHeader("Accept", "application/vnd.github.v3+json")
        client.addHeader("Authorization", "token b84c50b376ec691500f507663cb5dedf3e841656")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {
                        val user = responseObject.getJSONObject(i)
                        val userItem = UserItems(
                            user.getInt("id"),
                            user.getString("node_id"),
                            user.getString("login"),
                            user.getString("avatar_url")
                        )

                        listItems.add(userItem)
                    }

                    listFollowers.postValue(listItems)
                } catch (e: Exception) {
                    listFollowers.postValue(ArrayList())
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                listFollowers.postValue(ArrayList())
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getFollowers() : LiveData<ArrayList<UserItems>> {
        return listFollowers
    }
}