package com.example.prakpm_2417052021.data.model

import com.google.gson.annotations.SerializedName

data class Workout(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("image_url")
    val imageUrl: String
)
