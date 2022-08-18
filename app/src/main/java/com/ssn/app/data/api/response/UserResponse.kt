package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.User

data class UserResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("name")
    val fullName: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("photo")
    val photo: String? = null,
    @SerializedName("api_token")
    val bearerToken: String? = null
) {
    fun asDomain(): User = User(
        id = id.orZero(),
        username = username.orEmpty(),
        fullName = fullName.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        address = address.orEmpty(),
        photo = photo.orEmpty(),
        bearerToken = bearerToken.orEmpty()
    )
}
