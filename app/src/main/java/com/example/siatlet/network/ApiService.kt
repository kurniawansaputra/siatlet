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
        @Field("level") level: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/user/get_all")
    fun getAllUser (
        @Field("token") token: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("api/user/add")
    fun addUser (
        @Field("username") username: String,
        @Field("level") level: String,
        @Field("nama") name: String,
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
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/user/change_password")
    fun changePassword (
        @Field("id_user") idUser: String,
        @Field("level") level: String,
        @Field("password_baru") newPassword: String,
        @Field("token") token: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/user/reset_password")
    fun resetPassword (
        @Field("username") username: String,
        @Field("level") level: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/user/get_by_level")
    fun getUserByLevel(
        @Field("level") level: String,
    ): Call<UserByLevelResponse>

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
        @Field("waktu_lomba") date: String,
        @Field("id_pelatih") idTrainer: String
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
        @Field("waktu_lomba") date: String,
        @Field("id_pelatih") idTrainer: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/kriteria/get_all")
    fun getAllCriteria (
        @Field("token") token: String,
    ): Call<CriteriaResponse>

    @FormUrlEncoded
    @POST("api/kriteria/add")
    fun addCriteria (
        @Field("token") token: String,
        @Field("nama_kriteria") name: String,
        @Field("sifat") property: String,
        @Field("id_lomba") idContest: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/kriteria/get_by_id")
    fun getCriteriaById(
        @Field("token") token: String,
        @Field("id_kriteria") idCriteria: String
    ): Call<CriteriaByIdResponse>

    @FormUrlEncoded
    @POST("api/kriteria/delete")
    fun deleteCriteria(
        @Field("token") token: String,
        @Field("id_kriteria") idCriteria: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/kriteria/update")
    fun updateCriteria (
        @Field("token") token: String,
        @Field("nama_kriteria") name: String,
        @Field("sifat") property: String,
        @Field("id_lomba") idContest: String,
        @Field("id_kriteria") idCriteria: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/peserta/get_all")
    fun getAllParticipant (
        @Field("token") token: String,
    ): Call<ParticipantResponse>
}