package com.example.mysharedpreferences

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var name: String? = null,
    var email: String? = null,
    var age: Int? = null,
    var phoneNumber: String? = null,
    var isLove: Boolean = false
) : Parcelable