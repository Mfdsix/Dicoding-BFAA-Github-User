package com.zgenit.githubuser.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfile(
    var id: Int = 0,
    var nodeId: String? = null,
    var username: String? = null,
    var fullname: String? = null,
    var avatar: String? = null,
    var publicRepos: Int? = null,
    var followers: Int? = null,
    var following: Int? = null,
    var office: String? = null,
    var location: String? = null
): Parcelable