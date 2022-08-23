package com.ssn.app.data.api.config

import com.ssn.app.data.api.request.EditProfileRequest
import com.ssn.app.data.api.request.RegisterRequest
import com.ssn.app.data.api.response.JobVacancyDetailResponse
import com.ssn.app.data.api.response.JobVacancyResponse
import com.ssn.app.data.api.response.TrainingDetailResponse
import com.ssn.app.data.api.response.TrainingFollowingDetailResponse
import com.ssn.app.data.api.response.TrainingFollowingResponse
import com.ssn.app.data.api.response.TrainingResponse
import com.ssn.app.data.api.response.UserResponse
import com.ssn.app.data.api.response.base.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<UserResponse>>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<BaseResponse<Any>>

    @GET("profile")
    suspend fun getProfile(): Response<BaseResponse<UserResponse>>

    @PUT("profile/edit")
    suspend fun updateProfile(
        @Body editProfileRequest: EditProfileRequest
    ): Response<BaseResponse<UserResponse>>

    @Multipart
    @POST("profile/avatar/edit")
    suspend fun updateAvatar(
        @Part photo: MultipartBody.Part
    ): Response<BaseResponse<String>>

    @GET("vacancies")
    suspend fun getJobVacancyList(): Response<BaseResponse<List<JobVacancyResponse>>>

    @GET("vacancy/{id}")
    suspend fun getJobVacancyDetail(
        @Path("id") id: Int
    ): Response<BaseResponse<JobVacancyDetailResponse>>

    @GET("trainings")
    suspend fun getTrainingList(): Response<BaseResponse<List<TrainingResponse>>>

    @GET("training/{id}")
    suspend fun getTrainingDetail(
        @Path("id") id: Int
    ): Response<BaseResponse<TrainingDetailResponse>>

    @Multipart
    @POST("training/register")
    suspend fun registerTraining(
        @Part("training_id") trainingId: RequestBody,
        @Part invoiceProof: MultipartBody.Part
    ): Response<BaseResponse<Any>>

    @GET("followup_trainings")
    suspend fun getFollowingTrainingList(): Response<BaseResponse<List<TrainingFollowingResponse>>>

    @GET("followup_training/{training_record_id}")
    suspend fun getFollowingTrainingDetail(
        @Path("training_record_id") id: Int
    ): Response<BaseResponse<TrainingFollowingDetailResponse>>

    @Multipart
    @POST("training/requirement")
    suspend fun uploadTrainingRequirement(
        @Part cv: MultipartBody.Part,
        @Part ktp: MultipartBody.Part,
        @Part ijazah: MultipartBody.Part,
        @Part workExperience: MultipartBody.Part,
        @Part portfolio: MultipartBody.Part,
        @Part optionalFile: MultipartBody.Part? = null
    ): Response<BaseResponse<Any>>
}
