package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class ParticipantRankingResponse(

	@field:SerializedName("data")
	val data: List<DataParticipantRanking?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataParticipantRanking(

	@field:SerializedName("id_peserta")
	val idPeserta: String? = null,

	@field:SerializedName("nama_peserta")
	val namaPeserta: String? = null,

	@field:SerializedName("ranking")
	val ranking: Int? = null,

	@field:SerializedName("hasil")
	val hasil: String? = null
)