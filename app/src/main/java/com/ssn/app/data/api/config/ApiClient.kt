package com.ssn.app.data.api.config

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ssn.app.data.api.config.interceptor.ApiInterceptor
import com.ssn.app.data.api.response.base.BaseResponse
import com.ssn.app.helper.Helper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://sisurty.herokuapp.com/api/v1/"

    private lateinit var appContext: Context
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    fun getApiService(): ApiService = apiService

    fun init(context: Context) {
        appContext = context
        initOkHttpClient()
        initRetrofit()
        initApiService()
    }

    private fun initOkHttpClient() {
        okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (Helper.isDebugBuild()) level = HttpLoggingInterceptor.Level.BODY
                }
            )
            addInterceptor(ApiInterceptor())
            addInterceptor(
                ChuckerInterceptor.Builder(appContext)
                    .collector(ChuckerCollector(appContext))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        }.build()
    }

    private fun initRetrofit() {
        retrofit = Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    private fun initApiService() {
        apiService = retrofit.create(ApiService::class.java)
    }

    fun <ResponseType> Call<BaseResponse<ResponseType>>.fetchResult(
        onSuccess: (BaseResponse<ResponseType>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        enqueue(object : Callback<BaseResponse<ResponseType>> {
            override fun onResponse(
                call: Call<BaseResponse<ResponseType>>,
                response: Response<BaseResponse<ResponseType>>
            ) {
                val body = response.body()
                if (body?.meta?.code != 200) {
                    onError.invoke(Throwable(body?.meta?.message ?: "Undefined Error"))
                } else {
                    onSuccess.invoke(body)
                }
            }

            override fun onFailure(call: Call<BaseResponse<ResponseType>>, t: Throwable) {
                onError.invoke(t)
            }
        })
    }
}
