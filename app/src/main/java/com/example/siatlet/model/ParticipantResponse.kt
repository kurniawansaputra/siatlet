package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class ParticipantResponse(

	@field:SerializedName("data")
	val data: List<DataParticipant?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataParticipant(

	@field:SerializedName("tempat_lahir")
	val tempatLahir: String? = null,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_peserta")
	val idPeserta: String? = null,

	@field:SerializedName("berat_badan")
	val beratBadan: String? = null,

	@field:SerializedName("nama_peserta")
	val namaPeserta: String? = null,

	@field:SerializedName("no_reg")
	val noReg: String? = null,

	@field:SerializedName("id_lomba")
	val idLomba: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)
