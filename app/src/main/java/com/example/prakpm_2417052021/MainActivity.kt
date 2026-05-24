package com.example.prakpm_2417052021

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.prakpm_2417052021.data.model.Workout
import com.example.prakpm_2417052021.data.repository.WorkoutRepository
import com.example.prakpm_2417052021.ui.theme.PrakPM_2417052021Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DailyWorkoutGoal = 3

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakPM_2417052021Theme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val repository = remember { WorkoutRepository() }
    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    var favoriteNames by remember { mutableStateOf<Set<String>>(emptySet()) }
    var completedNames by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var reloadKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(reloadKey) {
        isLoading = true
        isError = false
        try {
            workouts = repository.getWorkouts()
            isError = workouts.isEmpty()
        } catch (_: Exception) {
            workouts = emptyList()
            isError = true
        } finally {
            isLoading = false
        }
    }

    val toggleFavorite: (Workout) -> Unit = { workout ->
        favoriteNames = if (favoriteNames.contains(workout.nama)) {
            favoriteNames - workout.nama
        } else {
            favoriteNames + workout.nama
        }
    }

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            WorkoutList(
                workouts = workouts,
                favoriteNames = favoriteNames,
                completedCount = completedNames.size,
                isLoading = isLoading,
                isError = isError,
                onRetry = { reloadKey++ },
                onToggleFavorite = toggleFavorite,
                navController = navController
            )
        }
        composable(
            route = "detail/{workoutName}",
            arguments = listOf(navArgument("workoutName") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutName = Uri.decode(backStackEntry.arguments?.getString("workoutName") ?: "")
            val workout = workouts.find { it.nama == workoutName }
            if (workout != null) {
                DetailScreen(
                    workout = workout,
                    isFavorite = favoriteNames.contains(workout.nama),
                    snackbarHostState = snackbarHostState,
                    navController = navController,
                    onToggleFavorite = { toggleFavorite(workout) },
                    onWorkoutCompleted = {
                        completedNames = completedNames + workout.nama
                    }
                )
            } else {
                DetailNotFound(navController = navController)
            }
        }
    }
}

@Composable
fun WorkoutList(
    workouts: List<Workout>,
    favoriteNames: Set<String>,
    completedCount: Int,
    isLoading: Boolean,
    isError: Boolean,
    onRetry: () -> Unit,
    onToggleFavorite: (Workout) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    when {
        isLoading -> LoadingScreen(modifier = modifier)
        isError || workouts.isEmpty() -> ErrorScreen(onRetry = onRetry, modifier = modifier)
        else -> WorkoutContent(
            workouts = workouts,
            favoriteNames = favoriteNames,
            completedCount = completedCount,
            onToggleFavorite = onToggleFavorite,
            modifier = modifier,
            navController = navController
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Menyiapkan rencana latihan...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gagal Memuat Data",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Pastikan koneksi internet Anda menyala agar daftar latihan dan gambar dapat dimuat.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}

@Composable
fun DetailNotFound(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Data latihan tidak ditemukan",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Kembali")
        }
    }
}

@Composable
fun WorkoutContent(
    workouts: List<Workout>,
    favoriteNames: Set<String>,
    completedCount: Int,
    onToggleFavorite: (Workout) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedCategory by remember(workouts) { mutableStateOf("Semua") }
    val categories = remember(workouts) {
        listOf("Semua") + workouts.map { it.categoryLabel() }.distinct()
    }
    val filteredWorkouts = remember(workouts, selectedCategory) {
        if (selectedCategory == "Semua") {
            workouts
        } else {
            workouts.filter { it.categoryLabel() == selectedCategory }
        }
    }
    val todayPlan = remember(workouts) { workouts.take(DailyWorkoutGoal) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DashboardHeader(
                completedCount = completedCount,
                favoriteCount = favoriteNames.size,
                planMinutes = todayPlan.sumOf { it.durationValue() }
            )
        }

        item {
            SectionTitle(
                title = "Fokus Hari Ini",
                subtitle = "Mulai dari latihan ringan yang mudah dijaga konsistensinya."
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(todayPlan) { workout ->
                    WorkoutRowItem(
                        workout = workout,
                        isFavorite = favoriteNames.contains(workout.nama),
                        onToggleFavorite = { onToggleFavorite(workout) },
                        navController = navController
                    )
                }
            }
        }

        item {
            SectionTitle(
                title = "Pilih Fokus",
                subtitle = "Filter latihan sesuai target tubuh dan energi hari ini."
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }
        }

        item {
            SectionTitle(
                title = "Semua Latihan",
                subtitle = "${filteredWorkouts.size} latihan tersedia"
            )
        }

        items(filteredWorkouts) { workout ->
            WorkoutItem(
                workout = workout,
                isFavorite = favoriteNames.contains(workout.nama),
                onToggleFavorite = { onToggleFavorite(workout) },
                navController = navController
            )
        }
    }
}

@Composable
fun DashboardHeader(
    completedCount: Int,
    favoriteCount: Int,
    planMinutes: Int
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.jadwalin_hero),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Jadwalin",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Rencanakan latihan singkat, pantau progres, dan mulai bergerak tanpa bingung memilih menu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryCard(
                    title = "Sesi",
                    value = "${completedCount.coerceAtMost(DailyWorkoutGoal)}/$DailyWorkoutGoal",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Durasi",
                    value = "$planMinutes mnt",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Favorit",
                    value = "$favoriteCount",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { completedCount.coerceAtMost(DailyWorkoutGoal).toFloat() / DailyWorkoutGoal },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun WorkoutImage(
    workout: Workout,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = workout.imageUrl.takeIf { it.isNotBlank() },
        contentDescription = workout.nama,
        placeholder = painterResource(R.drawable.loading),
        error = painterResource(R.drawable.error),
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun WorkoutRowItem(
    workout: Workout,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .width(178.dp)
            .clickable { navController.navigate("detail/${Uri.encode(workout.nama)}") }
    ) {
        Column {
            Box {
                WorkoutImage(
                    workout = workout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                )
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onToggleFavorite,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = workout.categoryLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = workout.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = workout.durationLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Workout,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("detail/${Uri.encode(workout.nama)}") }
                .padding(14.dp)
        ) {
            WorkoutImage(
                workout = workout,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 108.dp, height = 122.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = workout.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onClick = onToggleFavorite
                    )
                }
                Text(
                    text = workout.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoChip(workout.categoryLabel())
                    InfoChip(workout.durationLabel())
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Target: ${workout.targetLabel()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DetailScreen(
    workout: Workout,
    isFavorite: Boolean,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    onToggleFavorite: () -> Unit,
    onWorkoutCompleted: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            WorkoutImage(
                workout = workout,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = workout.nama,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoChip(workout.categoryLabel())
            InfoChip(workout.durationLabel())
            InfoChip(workout.levelLabel())
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = workout.deskripsi,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(18.dp))

        DetailPanel(title = "Target Latihan") {
            Text(
                text = workout.targetLabel(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        DetailPanel(title = "Gerakan Inti") {
            workout.stepList().forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { uriHandler.openUri(workout.videoLink()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lihat Video Gerakan")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    delay(2000)
                    onWorkoutCompleted()
                    snackbarHostState.showSnackbar("Sesi ${workout.nama} selesai. Progres hari ini diperbarui.")
                    isLoading = false
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Menyimpan progres...")
            } else {
                Text("Tandai Selesai")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text("Kembali")
        }
    }
}

@Composable
fun DetailPanel(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

private fun Workout.categoryLabel(): String {
    val apiCategory = category?.takeIf { it.isNotBlank() }
    if (apiCategory != null) return apiCategory

    val name = nama.lowercase()
    return when {
        name.contains("push") || name.contains("pull") -> "Tubuh Atas"
        name.contains("sit") || name.contains("plank") -> "Core"
        name.contains("squat") || name.contains("lunge") -> "Kaki"
        else -> "Full Body"
    }
}

private fun Workout.durationValue(): Int {
    val apiDuration = durationMinutes?.takeIf { it > 0 }
    if (apiDuration != null) return apiDuration

    val name = nama.lowercase()
    return when {
        name.contains("plank") -> 4
        name.contains("pull") -> 8
        name.contains("squat") -> 7
        else -> 6
    }
}

private fun Workout.durationLabel(): String = "${durationValue()} mnt"

private fun Workout.levelLabel(): String {
    val apiLevel = level?.takeIf { it.isNotBlank() }
    if (apiLevel != null) return apiLevel

    val name = nama.lowercase()
    return when {
        name.contains("pull") -> "Menengah"
        name.contains("plank") -> "Pemula"
        else -> "Ringan"
    }
}

private fun Workout.targetLabel(): String {
    val apiTarget = target?.takeIf { it.isNotBlank() }
    if (apiTarget != null) return apiTarget

    val name = nama.lowercase()
    return when {
        name.contains("push") -> "Dada, bahu, tricep"
        name.contains("sit") -> "Perut dan fleksor pinggul"
        name.contains("pull") -> "Punggung, lengan, dan grip"
        name.contains("plank") -> "Core, bahu, dan stabilitas tubuh"
        name.contains("squat") -> "Paha, glute, dan kekuatan kaki"
        else -> "Kekuatan dan daya tahan tubuh"
    }
}

private fun Workout.videoLink(): String {
    val directUrl = videoUrl?.takeIf { it.isNotBlank() }
    if (directUrl != null) return directUrl

    return "https://www.youtube.com/results?search_query=${
        Uri.encode("$nama tutorial gerakan olahraga singkat")
    }"
}

private fun Workout.stepList(): List<String> {
    val apiSteps = steps?.filter { it.isNotBlank() }?.takeIf { it.isNotEmpty() }
    if (apiSteps != null) return apiSteps

    val name = nama.lowercase()
    return when {
        name.contains("push") -> listOf(
            "Posisikan telapak tangan sejajar bahu dan tubuh lurus.",
            "Turunkan dada perlahan sampai siku menekuk stabil.",
            "Dorong tubuh naik lagi tanpa mengangkat pinggul terlalu tinggi."
        )
        name.contains("sit") -> listOf(
            "Tekuk lutut dan posisikan telapak kaki menapak.",
            "Angkat badan memakai otot perut, bukan menarik leher.",
            "Turunkan badan perlahan agar gerakan tetap terkontrol."
        )
        name.contains("pull") -> listOf(
            "Genggam palang sedikit lebih lebar dari bahu.",
            "Tarik tubuh sampai dagu melewati palang.",
            "Turunkan tubuh perlahan sampai lengan hampir lurus."
        )
        name.contains("plank") -> listOf(
            "Letakkan siku di bawah bahu dan luruskan tubuh.",
            "Kencangkan perut agar pinggang tidak turun.",
            "Tahan posisi sambil bernapas stabil."
        )
        name.contains("squat") -> listOf(
            "Buka kaki selebar bahu dan arahkan lutut mengikuti jari kaki.",
            "Turunkan pinggul seperti duduk tanpa membungkukkan punggung.",
            "Dorong tumit untuk kembali berdiri."
        )
        else -> listOf(
            "Mulai dengan pemanasan singkat.",
            "Lakukan gerakan secara terkontrol sesuai kemampuan.",
            "Berhenti jika terasa nyeri tajam atau pusing."
        )
    }
}
