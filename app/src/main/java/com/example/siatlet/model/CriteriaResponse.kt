package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class CriteriaResponse(

	@field:SerializedName("data")
	val data: List<DataCriteria?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataCriteria(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("sifat")
	val sifat: String? = null,

	@field:SerializedName("nama_kriteria")
	val namaKriteria: String? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("nama_lomba")
	val namaLomba: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_kriteria")
	val idKriteria: String? = null
)
