package com.example.prakpm_2417052021.data.model

import com.google.gson.annotations.SerializedName

data class Workout(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName(value = "kategori", alternate = ["category"])
    val category: String? = null,

    @SerializedName(value = "durasi_menit", alternate = ["duration_minutes"])
    val durationMinutes: Int? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("target")
    val target: String? = null,

    @SerializedName("video_url")
    val videoUrl: String? = null,

    @SerializedName("steps")
    val steps: List<String>? = null
)
