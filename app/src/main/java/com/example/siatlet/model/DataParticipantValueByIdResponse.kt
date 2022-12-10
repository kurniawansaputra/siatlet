package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class DataParticipantValueByIdResponse(

	@field:SerializedName("data")
	val data: List<DataParticipantValue?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataParticipantValue(

	@field:SerializedName("id_peserta")
	val idPeserta: String? = null,

	@field:SerializedName("id_nilai_peserta")
	val idNilaiPeserta: String? = null,

	@field:SerializedName("nama_peserta")
	val namaPeserta: String? = null,

	@field:SerializedName("nama_kriteria")
	val namaKriteria: String? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("nama_lomba")
	val namaLomba: String? = null,

	@field:SerializedName("nilai_kriteria_peserta")
	val nilaiKriteriaPeserta: String? = null,

	@field:SerializedName("ket_nilai_kriteria")
	val ketNilaiKriteria: String? = null,

	@field:SerializedName("id_kriteria")
	val idKriteria: String? = null
)
