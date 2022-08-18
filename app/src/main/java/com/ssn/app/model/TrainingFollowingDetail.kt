package com.ssn.app.model

data class TrainingFollowingDetail(
    val id: Int,
    val trainingName: String,
    val trainingImage: String,
    val trainingDescription: String,
    val trainingStartDate: String,
    val trainingEndDate: String,
    val trainerName: String,
    val trainingStatus: String,
    val trainerCv: String,
    val trainingBook: String,
    val requirementStatus: String,
    val trainingCertificate: String,
    val competenceCertificate: String
)