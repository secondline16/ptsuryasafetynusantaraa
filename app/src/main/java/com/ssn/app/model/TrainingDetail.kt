package com.ssn.app.model

data class TrainingDetail(
    val id: Int,
    val trainingImage: String,
    val trainingName: String,
    val trainingStartDate: String,
    val trainingEndDate: String,
    val trainingPrice: Int,
    val trainingDescription: String,
    val trainingStatus: Boolean
)