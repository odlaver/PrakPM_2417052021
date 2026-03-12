package com.example.prakpm_2417052021

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prakpm_2417052021.ui.theme.PrakPM_2417052021Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakPM_2417052021Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Greeting()
                        WorkoutList()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(
        text = "Selamat datang di Jadwalin, berikut rekomendasi latihan untuk menjaga kesehatanmu!",
        modifier = Modifier.padding(16.dp)
    )
}

data class Workout(val nama: String,val deskripsi: String,val imageRes: Int)

object WorkoutSource {
    val workouts = listOf(
        Workout("Push Up", "Latihan untuk otot dada dan tricep", R.drawable.pushup),
        Workout("Sit Up", "Latihan untuk otot perut", R.drawable.situp),
        Workout("Pull Up", "Latihan untuk otot punggung", R.drawable.pullup),
        Workout("Plank", "Latihan untuk kekuatan core", R.drawable.plank),
        Workout("Squat", "Latihan untuk otot paha dan kaki", R.drawable.squat)
    )
}

@Composable
fun WorkoutList() {
    LazyColumn {
        items(WorkoutSource.workouts) { workout ->
            WorkoutItem(workout)
        }
    }
}

@Composable
fun WorkoutItem(workout: Workout) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Image(
            painter = painterResource(id = workout.imageRes),
            contentDescription = workout.nama,
            modifier = Modifier.size(width = 120.dp, height = 120.dp)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)        ) {
            Text(text = workout.nama)
            Text(text = workout.deskripsi)
            Button(onClick = { }) {
                Text("Mulai Latihan")
            }
        }
    }
}