package com.ssn.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val photo: String,
    val bearerToken: String
) : Parcelable
