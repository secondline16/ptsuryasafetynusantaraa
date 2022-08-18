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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    fun login(
        @Query("email") email: String, // TODO Request untuk diubah jadi username sesuai design, bukan email
        @Query("password") password: String
    ): Call<BaseResponse<UserResponse>> // TODO Request untuk disamakan responsenya dengan get profile

    @POST("auth/register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<BaseResponse<Any>>

    @GET("profile")
    fun getProfile(): Call<BaseResponse<UserResponse>>

    @PUT("profile/edit")
    fun updateProfile(
        @Body editProfileRequest: EditProfileRequest
    ): Call<BaseResponse<UserResponse>>

    // TODO ask BE error
    @Multipart
    @POST("profile/avatar/edit")
    fun updateAvatar(
        @Part photo: MultipartBody.Part
    ): Call<BaseResponse<String>>

    @GET("vacancies")
    fun getJobVacancyList(): Call<BaseResponse<List<JobVacancyResponse>>>

    @GET("vacancy/{id}")
    fun getJobVacancyDetail(
        @Path("id") id: Int
    ): Call<BaseResponse<JobVacancyDetailResponse>>

    @GET("trainings")
    fun getTrainingList(): Call<BaseResponse<List<TrainingResponse>>>

    @GET("training/{id}")
    fun getTrainingDetail(
        @Path("id") id: Int
    ): Call<BaseResponse<TrainingDetailResponse>>

    // TODO ask BE error invoice proof failed to upload and customer id from?
    @Multipart
    @POST("training/register")
    fun registerTraining(
        @Part("training_id") trainingId: RequestBody,
        @Part invoiceProof: MultipartBody.Part
    ): Call<BaseResponse<Any>>

    @GET("followup_trainings")
    fun getFollowingTrainingList(): Call<BaseResponse<List<TrainingFollowingResponse>>>

    @GET("followup_training/{training_record_id}")
    fun getFollowingTrainingDetail(
        @Path("training_record_id") id: Int
    ): Call<BaseResponse<TrainingFollowingDetailResponse>>

    @Multipart
    @POST("training/requirement")
    fun uploadTrainingRequirement(
        @Part cv: MultipartBody.Part,
        @Part ktp: MultipartBody.Part,
        @Part ijazah: MultipartBody.Part,
        @Part workExperience: MultipartBody.Part,
        @Part portfolio: MultipartBody.Part,
        @Part optionalFile: MultipartBody.Part? = null
    ): Call<BaseResponse<Any>>
}
