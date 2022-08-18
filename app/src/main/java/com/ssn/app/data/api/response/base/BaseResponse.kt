package com.ssn.app.data.api.response.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("meta")
    val meta: MetaResponse? = null,
    @SerializedName("data")
    val data: T? = null
) {
    data class MetaResponse(
        @SerializedName("code")
        val code: Int? = null,
        @SerializedName("message")
        val message: String? = null
    )
}