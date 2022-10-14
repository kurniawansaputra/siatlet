package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class CriteriaByIdResponse(

	@field:SerializedName("data")
	val data: DataCriteriaById? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataCriteriaById(

	@field:SerializedName("sifat")
	val sifat: String? = null,

	@field:SerializedName("nama_kriteria")
	val namaKriteria: String? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("nama_lomba")
	val namaLomba: String? = null,

	@field:SerializedName("id_kriteria")
	val idKriteria: String? = null
)
