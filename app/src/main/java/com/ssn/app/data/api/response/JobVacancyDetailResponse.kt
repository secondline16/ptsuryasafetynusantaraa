package com.ssn.app.data.api.response

import com.google.gson.annotations.SerializedName
import com.ssn.app.extension.orZero
import com.ssn.app.model.JobVacancyDetail

data class JobVacancyDetailResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("company_name")
    val companyName: String? = null,
    @SerializedName("job_position")
    val jobPosition: String? = null,
    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("requirements")
    val requirements: String? = null
) {
    fun asDomain(): JobVacancyDetail = JobVacancyDetail(
        id = id.orZero(),
        companyName = companyName.orEmpty(),
        jobPosition = jobPosition.orEmpty(),
        deadline = deadline.orEmpty(),
        description = description.orEmpty(),
        requirements = requirements.orEmpty()
    )
}
