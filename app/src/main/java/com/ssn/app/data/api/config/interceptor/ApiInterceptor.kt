package com.ssn.app.data.api.config.interceptor

import com.ssn.app.data.preference.SharedPrefProvider
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class ApiInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder().build()
        val bearerToken = SharedPrefProvider.getUser().bearerToken
        val requestBuilder = original.newBuilder().apply {
            addHeader(name = "Authorization", value = "Bearer $bearerToken")
            addHeader(name = "Content-Type", value = "application/json")
            url(url)
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
