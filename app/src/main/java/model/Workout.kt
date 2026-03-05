package model
import androidx.annotation.DrawableRes
data class Workout(
    val nama: String,
    val deskripsi: String,
    @DrawableRes val imageRes: Int

)
