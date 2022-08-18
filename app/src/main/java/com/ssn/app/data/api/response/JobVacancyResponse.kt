package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.JobVacancy

data class JobVacancyResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("company_name")
    val companyName: String? = null,
    @SerializedName("job_position")
    val jobPosition: String? = null,
    @SerializedName("deadline")
    val deadline: String? = null
) {
    fun asDomain(): JobVacancy = JobVacancy(
        id = id.orZero(),
        companyName = companyName.orEmpty(),
        jobPosition = jobPosition.orEmpty(),
        deadline = deadline.orEmpty()
    )
}