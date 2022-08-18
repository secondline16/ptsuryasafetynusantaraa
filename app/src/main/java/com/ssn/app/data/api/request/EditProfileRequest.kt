package com.ssn.app.data.api.request

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("address")
    val address: String
)