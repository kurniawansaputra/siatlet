package com.example.siatlet.model

import com.google.gson.annotations.SerializedName

data class Meta(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)