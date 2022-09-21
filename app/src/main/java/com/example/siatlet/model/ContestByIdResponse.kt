package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class ContestByIdResponse(

	@field:SerializedName("data")
	val data: DataContestById? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataContestById(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("nama_lomba")
	val namaLomba: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("waktu_lomba")
	val waktuLomba: String? = null
)
