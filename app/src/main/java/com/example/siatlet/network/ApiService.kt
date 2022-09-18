package com.example.siatlet.network

import com.example.siatlet.model.LoginResponse
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.model.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api/auth/login")
    fun login (
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/user/get_all")
    fun getAllUser (
        @Field("token") token: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("api/user/add")
    fun addUser (
        @Field("token") token: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("level") level: String,
        @Field("nama") name: String,
        @Field("no_hp") phone: String,
        @Field("alamat") address: String,
        @Field("jenis_kelamin") gender: String
    ): Call<MetaResponse>
}