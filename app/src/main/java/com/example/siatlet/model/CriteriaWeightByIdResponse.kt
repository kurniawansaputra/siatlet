package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class CriteriaWeightByIdResponse(

	@field:SerializedName("data")
	val data: DataCriteriaWeightById? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataCriteriaWeightById(

	@field:SerializedName("bobot")
	val bobot: String? = null,

	@field:SerializedName("nama_kriteria")
	val namaKriteria: String? = null,

	@field:SerializedName("id_bobot")
	val idBobot: String? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("nama_lomba")
	val namaLomba: String? = null,

	@field:SerializedName("id_kriteria")
	val idKriteria: String? = null
)
