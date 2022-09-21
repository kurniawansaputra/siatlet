package com.example.siatlet.network

import com.example.siatlet.model.*
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

    @FormUrlEncoded
    @POST("api/user/delete")
    fun deleteUser(
        @Field("token") token: String,
        @Field("id_user") idUser: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/user/get_by_id")
    fun getUserById(
        @Field("token") token: String,
        @Field("id_user") idUser: String
    ): Call<UserByIdResponse>

    @FormUrlEncoded
    @POST("api/user/update")
    fun updateUser(
        @Field("token") token: String,
        @Field("id_user") idUser: String,
        @Field("username") username: String,
        @Field("level") level: String,
        @Field("nama") name: String,
        @Field("no_hp") phone: String,
        @Field("alamat") address: String,
        @Field("jenis_kelamin") gender: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/lomba/get_all")
    fun getAllContest(
        @Field("token") token: String,
    ): Call<ContestResponse>

    @FormUrlEncoded
    @POST("api/lomba/add")
    fun addContest (
        @Field("token") token: String,
        @Field("nama_lomba") name: String,
        @Field("waktu_lomba") date: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/lomba/delete")
    fun deleteContest(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/lomba/get_by_id")
    fun getContestById(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String
    ): Call<ContestByIdResponse>

    @FormUrlEncoded
    @POST("api/lomba/update")
    fun updateContest(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String,
        @Field("nama_lomba") name: String,
        @Field("waktu_lomba") date: String
    ): Call<MetaResponse>
}