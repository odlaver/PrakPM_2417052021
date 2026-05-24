package com.example.prakpm_2417052021.data.repository

import com.example.prakpm_2417052021.data.api.RetrofitClient
import com.example.prakpm_2417052021.data.model.Workout

class WorkoutRepository {
    suspend fun getWorkouts(): List<Workout> {
        return try {
            RetrofitClient.instance.getWorkouts()
        } catch (_: Exception) {
            emptyList()
        }
    }
}
