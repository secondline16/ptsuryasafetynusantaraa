package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.TrainingFollowingDetail

data class TrainingFollowingDetailResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("training_name")
    val trainingName: String? = null,
    @SerializedName("training_img")
    val trainingImage: String? = null,
    @SerializedName("training_desc")
    val trainingDescription: String? = null,
    @SerializedName("training_start")
    val trainingStartDate: String? = null,
    @SerializedName("training_end")
    val trainingEndDate: String? = null,
    @SerializedName("trainer_name")
    val trainerName: String? = null,
    @SerializedName("status")
    val trainingStatus: String? = null,
    @SerializedName("trainer_cv")
    val trainerCv: String? = null,
    @SerializedName("training_book")
    val trainingBook: String? = null,
    @SerializedName("requirement_status")
    val requirementStatus: String? = null,
    @SerializedName("training_certificate")
    val trainingCertificate: String? = null,
    @SerializedName("competence_certificate")
    val competenceCertificate: String? = null
) {
    fun asDomain(): TrainingFollowingDetail = TrainingFollowingDetail(
        id = id.orZero(),
        trainerName = trainerName.orEmpty(),
        trainingImage = trainingImage.orEmpty(),
        trainingStatus = trainingStatus.orEmpty(),
        trainingDescription = trainingDescription.orEmpty(),
        trainingStartDate = trainingStartDate.orEmpty(),
        trainingEndDate = trainingEndDate.orEmpty(),
        trainerCv = trainerCv.orEmpty(),
        trainingBook = trainingBook.orEmpty(),
        requirementStatus = requirementStatus.orEmpty(),
        trainingCertificate = trainingCertificate.orEmpty(),
        competenceCertificate = competenceCertificate.orEmpty(),
        trainingName = trainingName.orEmpty()
    )
}
