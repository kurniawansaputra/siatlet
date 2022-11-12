package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class CriteriaValueByIdResponse(

	@field:SerializedName("data")
	val data: DataCriteriaValueById? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataCriteriaValueById(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("nama_kriteria")
	val namaKriteria: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("id_kriteria")
	val idKriteria: String? = null,

	@field:SerializedName("id_nilai_kriteria")
	val idNilaiKriteria: String? = null
)
