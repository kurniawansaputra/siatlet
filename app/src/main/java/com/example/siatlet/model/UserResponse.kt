package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: List<DataUser?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataUser(

	@field:SerializedName("password_encrypt")
	val passwordEncrypt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("password_decrypt")
	val passwordDecrypt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
