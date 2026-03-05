package model
import com.example.prakpm_2417052021.R

object WorkoutSource {
    val workouts = listOf(
        Workout(
            nama = "Push Up",
            deskripsi = "Latihan untuk melatih otot dada, bahu, dan tricep",
            imageRes = R.drawable.pushup
        ),
        Workout(
            nama = "Sit Up",
            deskripsi = "Latihan untuk memperkuat otot perut",
            imageRes = R.drawable.situp
        ),
        Workout(
            nama = "Pull Up",
            deskripsi = "Latihan untuk melatih otot punggung dan lengan",
            imageRes = R.drawable.pullup
        ),
        Workout(
            nama = "Plank",
            deskripsi = "Latihan untuk melatih kekuatan core tubuh",
            imageRes = R.drawable.plank
        ),
        Workout(
            nama = "Squat",
            deskripsi = "Latihan untuk melatih otot paha dan kaki",
            imageRes = R.drawable.squat
        )
    )
}