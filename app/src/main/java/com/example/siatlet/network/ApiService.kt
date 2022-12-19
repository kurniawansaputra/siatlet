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
    ): Call<UserResponse>

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

    @FormUrlEncoded
    @POST("api/peserta/add")
    fun addParticipant (
        @Field("token") token: String,
        @Field("id_lomba") idContest: String,
        @Field("nama_peserta") name: String,
        @Field("tempat_lahir") placeOfBirth: String,
        @Field("tanggal_lahir") dateOfBirth: String,
        @Field("jenis_kelamin") gender: String,
        @Field("berat_badan") weight: String,
        @Field("alamat") address: String,
        @Field("pekerjaan") occupation: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/peserta/update")
    fun updateParticipant (
        @Field("token") token: String,
        @Field("id_peserta") idParticipant: String,
        @Field("id_lomba") idContest: String,
        @Field("no_reg") numberRegistration: String,
        @Field("nama_peserta") name: String,
        @Field("tempat_lahir") placeOfBirth: String,
        @Field("tanggal_lahir") dateOfBirth: String,
        @Field("jenis_kelamin") gender: String,
        @Field("berat_badan") weight: String,
        @Field("alamat") address: String,
        @Field("pekerjaan") occupation: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/peserta/get_by_id")
    fun getParticipantById (
        @Field("token") token: String,
        @Field("id_peserta") idParticipant: String
    ): Call<ParticipantByIdResponse>

    @FormUrlEncoded
    @POST("api/peserta/delete")
    fun deleteParticipant(
        @Field("token") token: String,
        @Field("id_peserta") idParticipant: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/peserta/peserta_by_lomba")
    fun getParticipantByIdContest (
        @Field("token") token: String,
        @Field("id_lomba") idContest: String
    ): Call<ParticipantResponse>

    @FormUrlEncoded
    @POST("api/lomba/get_by_pelatih")
    fun getContestByTrainer (
        @Field("id_pelatih") idTrainer: String,
        @Field("token") token: String,
    ): Call<ContestResponse>

    @FormUrlEncoded
    @POST("api/bobotkriteria/by_lomba")
    fun getCriteriaWeihgtByContest(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String
    ): Call<CriteriaWeightByContestResponse>

    @FormUrlEncoded
    @POST("api/bobotkriteria/add")
    fun addCriteriaWeight (
        @Field("token") token: String,
        @Field("id_lomba") idContest: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("bobot") weight: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/bobotkriteria/delete")
    fun deleteCriteriaWeight(
        @Field("token") token: String,
        @Field("id_bobot") idCriteriaWeight: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/bobotkriteria/get_by_id")
    fun getCriteriaWeightById(
        @Field("token") token: String,
        @Field("id_bobot") idCriteriaWeight: String
    ): Call<CriteriaWeightByIdResponse>

    @FormUrlEncoded
    @POST("api/kriteria/get_by_id_lomba")
    fun getCriteriaByIdContest(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String
    ): Call<CriteriaResponse>

    @FormUrlEncoded
    @POST("api/bobotkriteria/update")
    fun updateCriteriaWeight (
        @Field("token") token: String,
        @Field("id_bobot") idCriteriaWeight: String,
        @Field("id_lomba") idContest: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("bobot") weight: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaikriteria/get_by_id_kriteria")
    fun getCriteriaValueByIdCriteria(
        @Field("token") token: String,
        @Field("id_kriteria") idCriteria: String
    ): Call<CriteriaValueByIdCriteriaResponse>

    @FormUrlEncoded
    @POST("api/nilaikriteria/add")
    fun addCriteriaValue (
        @Field("token") token: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("jenis_kelamin") gender: String,
        @Field("nilai") value: String,
        @Field("keterangan") desc: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaikriteria/get_by_id")
    fun getCriteriaValueById(
        @Field("token") token: String,
        @Field("id_nilai_kriteria") idCriteriaValue: String
    ): Call<CriteriaValueByIdResponse>

    @FormUrlEncoded
    @POST("api/nilaikriteria/delete")
    fun deleteCriteriaValue(
        @Field("token") token: String,
        @Field("id_nilai_kriteria") idCriteriaValue: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaikriteria/update")
    fun updateCriteriaValue (
        @Field("token") token: String,
        @Field("id_nilai_kriteria") idCriteriaValue: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("jenis_kelamin") gender: String,
        @Field("nilai") value: String,
        @Field("keterangan") desc: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaipeserta/by_peserta")
    fun getDataParticipantValueById(
        @Field("token") token: String,
        @Field("id_peserta") idParticipant: String
    ): Call<DataParticipantValueByIdResponse>

    @FormUrlEncoded
    @POST("api/nilaipeserta/add")
    fun addParticipantValue (
        @Field("token") token: String,
        @Field("id_peserta") idParticipant: String,
        @Field("id_lomba") idContest: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("id_nilai_kriteria") idCriteriaValue: String,
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaipeserta/delete")
    fun deleteParticipantValue(
        @Field("token") token: String,
        @Field("id_nilai_peserta") idParticipantValue: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/nilaipeserta/get_by_id")
    fun getParticipantValueById(
        @Field("token") token: String,
        @Field("id_nilai_peserta") idParticipantValue: String
    ): Call<ParticipantValueByIdResponse>

    @FormUrlEncoded
    @POST("api/nilaipeserta/update")
    fun updateParticipantValue (
        @Field("token") token: String,
        @Field("id_nilai_peserta") idParticipantValue: String,
        @Field("id_peserta") idParticipant: String,
        @Field("id_lomba") idContest: String,
        @Field("id_kriteria") idCriteria: String,
        @Field("id_nilai_kriteria") idCriteriaValue: String
    ): Call<MetaResponse>

    @FormUrlEncoded
    @POST("api/hasil/ranking")
    fun participantRanking(
        @Field("token") token: String,
        @Field("id_lomba") idContest: String,
        @Field("jenis_kelamin") gender: String
    ): Call<ParticipantRankingResponse>
}