package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: Message? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Message(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
