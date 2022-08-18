package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.Training

data class TrainingResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("training_name")
    val trainingName: String? = null,
    @SerializedName("training_start")
    val trainingStartDate: String? = null,
    @SerializedName("training_end")
    val trainingEndDate: String? = null,
    @SerializedName("training_price")
    val trainingPrice: Int? = null
) {
    fun asDomain(): Training = Training(
        id = id.orZero(),
        trainingName = trainingName.orEmpty(),
        trainingStartDate = trainingStartDate.orEmpty(),
        trainingEndDate = trainingEndDate.orEmpty(),
        trainingPrice = trainingPrice.orZero()
    )
}