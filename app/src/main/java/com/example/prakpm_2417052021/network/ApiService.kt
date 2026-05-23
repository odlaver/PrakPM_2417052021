package com.example.prakpm_2417052021.network

import com.example.prakpm_2417052021.model.Workout
import retrofit2.http.GET

interface ApiService {
    @GET("menu_latihan.json")
    suspend fun getWorkouts(): List<Workout>
}
