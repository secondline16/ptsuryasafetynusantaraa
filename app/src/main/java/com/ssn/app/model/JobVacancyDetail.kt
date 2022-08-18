package com.ssn.app.model

data class JobVacancyDetail(
    val id: Int,
    val companyName: String,
    val jobPosition: String,
    val deadline: String,
    val description: String,
    val requirements: String
)
