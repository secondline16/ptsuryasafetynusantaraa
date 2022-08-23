package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.TrainingFollowing

data class TrainingFollowingResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("training_name")
    val trainingName: String? = null,
    @SerializedName("status")
    val trainingStatus: String? = null
) {
    fun asDomain(): TrainingFollowing = TrainingFollowing(
        id = id.orZero(),
        trainingName = trainingName.orEmpty(),
        trainingStatus = trainingStatus.orEmpty()
    )
}