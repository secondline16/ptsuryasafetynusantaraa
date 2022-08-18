package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orFalse
import com.ssn.app.extension.orZero
import com.ssn.app.model.TrainingDetail

data class TrainingDetailResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("training_img")
    val trainingImage: String? = null,
    @SerializedName("training_name")
    val trainingName: String? = null,
    @SerializedName("training_start")
    val trainingStartDate: String? = null,
    @SerializedName("training_end")
    val trainingEndDate: String? = null,
    @SerializedName("training_price")
    val trainingPrice: Int? = null,
    @SerializedName("training_desc")
    val trainingDescription: String? = null,
    @SerializedName("training_status")
    val trainingStatus: Boolean? = null
) {
    fun asDomain(): TrainingDetail = TrainingDetail(
        id = id.orZero(),
        trainingImage = trainingImage.orEmpty(),
        trainingName = trainingName.orEmpty(),
        trainingStartDate = trainingStartDate.orEmpty(),
        trainingEndDate = trainingEndDate.orEmpty(),
        trainingPrice = trainingPrice.orZero(),
        trainingDescription = trainingDescription.orEmpty(),
        trainingStatus = trainingStatus.orFalse()
    )
}