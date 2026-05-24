package com.example.prakpm_2417052021.data.repository

import com.example.prakpm_2417052021.data.api.RetrofitClient
import com.example.prakpm_2417052021.data.model.Workout

class WorkoutRepository {
    suspend fun getWorkouts(): List<Workout> {
        return try {
            val apiWorkouts = RetrofitClient.instance.getWorkouts()
            apiWorkouts
                .distinctBy { it.nama.lowercase() }
                .map { it.withDefaultDetails() }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun Workout.withDefaultDetails(): Workout {
        val name = nama.lowercase()
        return copy(
            category = category ?: defaultCategory(name),
            durationMinutes = durationMinutes ?: defaultDuration(name),
            level = level ?: defaultLevel(name),
            target = target ?: defaultTarget(name),
            videoUrl = videoUrl ?: "https://www.youtube.com/results?search_query=${nama}+tutorial+gerakan+singkat",
            steps = steps ?: defaultSteps(name)
        )
    }

    private fun defaultCategory(name: String): String {
        return when {
            name.contains("push") || name.contains("pull") -> "Tubuh Atas"
            name.contains("sit") || name.contains("plank") -> "Core"
            name.contains("squat") || name.contains("lunge") -> "Kaki"
            name.contains("mountain") -> "Full Body"
            else -> "Full Body"
        }
    }

    private fun defaultDuration(name: String): Int {
        return when {
            name.contains("plank") -> 4
            name.contains("pull") -> 8
            name.contains("squat") || name.contains("lunge") -> 7
            else -> 6
        }
    }

    private fun defaultLevel(name: String): String {
        return when {
            name.contains("pull") || name.contains("lunge") || name.contains("mountain") -> "Menengah"
            name.contains("plank") -> "Pemula"
            else -> "Ringan"
        }
    }

    private fun defaultTarget(name: String): String {
        return when {
            name.contains("push") -> "Dada, bahu, tricep"
            name.contains("sit") -> "Perut dan fleksor pinggul"
            name.contains("pull") -> "Punggung, lengan, dan grip"
            name.contains("plank") -> "Core, bahu, dan stabilitas tubuh"
            name.contains("squat") -> "Paha, glute, dan kekuatan kaki"
            name.contains("lunge") -> "Paha depan, hamstring, glute, dan keseimbangan"
            name.contains("mountain") -> "Core, bahu, paha, dan kardio"
            else -> "Kekuatan dan daya tahan tubuh"
        }
    }

    private fun defaultSteps(name: String): List<String> {
        return when {
            name.contains("lunge") -> listOf(
                "Langkahkan satu kaki ke depan dengan tubuh tetap tegak.",
                "Turunkan badan sampai kedua lutut menekuk stabil.",
                "Dorong kaki depan untuk kembali berdiri dan ganti sisi."
            )
            name.contains("mountain") -> listOf(
                "Mulai dari posisi plank tinggi dengan tangan sejajar bahu.",
                "Tarik lutut kanan ke arah dada lalu kembalikan.",
                "Ganti kaki secara bergantian dengan ritme terkontrol."
            )
            else -> null
        } ?: emptyList()
    }
}
