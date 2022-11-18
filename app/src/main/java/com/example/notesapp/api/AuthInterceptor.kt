package com.example.notesapp.api

import com.example.notesapp.utils.TokenManger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor : Interceptor {
    @Inject
    lateinit var tokenManger: TokenManger
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()

        val token = tokenManger.getToken()
        request.addHeader("authorization", "{Bearer$token}")

        return chain.proceed(request.build())
    }

}