package com.example.siatlet.network

import com.example.siatlet.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api/auth/login")
    fun login (
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}