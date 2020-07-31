package com.zgenit.githubuser.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItems(
    var id: Int = 0,
    var nodeId: String? = null,
    var username: String? = null,
    var avatar: String? = null
): Parcelable