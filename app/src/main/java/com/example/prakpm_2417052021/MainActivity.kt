package com.example.prakpm_2417052021

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.prakpm_2417052021.ui.theme.PrakPM_2417052021Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakPM_2417052021Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WorkoutList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
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
fun WorkoutList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Selamat datang di Jadwalin, berikut rekomendasi latihan untuk menjaga kesehatanmu!",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Rekomendasi Latihan",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WorkoutSource.workouts) { workout ->
                    WorkoutRowItem(workout)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Daftar Latihan Lengkap",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(WorkoutSource.workouts) { workout ->
            WorkoutItem(workout)
        }
    }
}

@Composable
fun WorkoutRowItem(workout: Workout) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.width(160.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = workout.imageRes),
                contentDescription = workout.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = workout.nama,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = workout.deskripsi,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun WorkoutItem(workout: Workout) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Box {
                Image(
                    painter = painterResource(id = workout.imageRes),
                    contentDescription = workout.nama,
                    modifier = Modifier.size(width = 120.dp, height = 120.dp)
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = workout.nama, fontWeight = FontWeight.Bold)
                Text(text = workout.deskripsi)
                Button(onClick = { }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Mulai Latihan")
                }
            }
        }
    }
}