package com.example.prakpm_2417052021

import android.app.Application
import android.content.Context
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.prakpm_2417052021.data.model.Workout
import com.example.prakpm_2417052021.data.repository.WorkoutRepository
import com.example.prakpm_2417052021.ui.theme.PrakPM_2417052021Theme
import com.example.prakpm_2417052021.ui.theme.CustomPrimary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        if (currentRoute == "home" || currentRoute == "activity" || currentRoute == "bmi" || currentRoute == "profile") {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                                    label = { Text("Utama") },
                                    selected = currentRoute == "home",
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                                    label = { Text("Aktivitas") },
                                    selected = currentRoute == "activity",
                                    onClick = {
                                        navController.navigate("activity") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                                    label = { Text("BMI") },
                                    selected = currentRoute == "bmi",
                                    onClick = {
                                        navController.navigate("bmi") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                                    label = { Text("Profil") },
                                    selected = currentRoute == "profile",
                                    onClick = {
                                        navController.navigate("profile") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
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

data class CompletedWorkout(
    val name: String,
    val timestamp: Long,
    val durationMinutes: Int,
    val category: String
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository()
    private val sharedPrefs = getApplication<Application>().getSharedPreferences("prakpm_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _favoriteNames = MutableStateFlow<Set<String>>(emptySet())
    val favoriteNames: StateFlow<Set<String>> = _favoriteNames.asStateFlow()

    private val _completedWorkouts = MutableStateFlow<List<CompletedWorkout>>(emptyList())
    val completedWorkouts: StateFlow<List<CompletedWorkout>> = _completedWorkouts.asStateFlow()

    private val _completedNames = MutableStateFlow<Set<String>>(emptySet())
    val completedNames: StateFlow<Set<String>> = _completedNames.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _dailyGoalMinutes = MutableStateFlow(15)
    val dailyGoalMinutes: StateFlow<Int> = _dailyGoalMinutes.asStateFlow()

    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake.asStateFlow()

    init {
        val favJson = sharedPrefs.getString("favorites", "[]")
        val favType = object : TypeToken<Set<String>>() {}.type
        val savedFavorites: Set<String> = gson.fromJson(favJson, favType) ?: emptySet()
        _favoriteNames.value = savedFavorites

        val compJson = sharedPrefs.getString("completed_workouts", "[]")
        val compType = object : TypeToken<List<CompletedWorkout>>() {}.type
        val savedCompleted: List<CompletedWorkout> = gson.fromJson(compJson, compType) ?: emptyList()
        _completedWorkouts.value = savedCompleted
        _completedNames.value = savedCompleted.map { it.name }.toSet()

        val todayStr = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
        _waterIntake.value = sharedPrefs.getInt("water_$todayStr", 0)
        _dailyGoalMinutes.value = sharedPrefs.getInt("daily_goal_minutes", 15)

        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                val data = repository.getWorkouts()
                _workouts.value = data
                _isError.value = data.isEmpty()
            } catch (_: Exception) {
                _workouts.value = emptyList()
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(workout: Workout) {
        val currentFavorites = _favoriteNames.value
        val newFavorites = if (currentFavorites.contains(workout.nama)) {
            currentFavorites - workout.nama
        } else {
            currentFavorites + workout.nama
        }
        _favoriteNames.value = newFavorites
        sharedPrefs.edit().putString("favorites", gson.toJson(newFavorites)).apply()
    }

    fun markCompleted(workout: Workout) {
        val newCompletedList = _completedWorkouts.value + CompletedWorkout(
            name = workout.nama,
            timestamp = System.currentTimeMillis(),
            durationMinutes = workout.durationValue(),
            category = workout.categoryLabel()
        )
        _completedWorkouts.value = newCompletedList
        _completedNames.value = _completedNames.value + workout.nama
        sharedPrefs.edit().putString("completed_workouts", gson.toJson(newCompletedList)).apply()
    }

    fun clearHistory() {
        _completedWorkouts.value = emptyList()
        _completedNames.value = emptySet()
        sharedPrefs.edit().remove("completed_workouts").apply()
    }

    fun getWorkoutStreak(workouts: List<CompletedWorkout>): Int {
        if (workouts.isEmpty()) return 0
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val days = workouts.map { sdf.format(java.util.Date(it.timestamp)) }.toSet()
        var streak = 0
        val cal = java.util.Calendar.getInstance()
        while (true) {
            val dateStr = sdf.format(cal.time)
            if (days.contains(dateStr)) {
                streak++
                cal.add(java.util.Calendar.DATE, -1)
            } else {
                if (streak == 0) {
                    cal.add(java.util.Calendar.DATE, -1)
                    val yesterdayStr = sdf.format(cal.time)
                    if (days.contains(yesterdayStr)) {
                        cal.add(java.util.Calendar.DATE, -1)
                        streak = 1
                        while (true) {
                            val nextDateStr = sdf.format(cal.time)
                            if (days.contains(nextDateStr)) {
                                streak++
                                cal.add(java.util.Calendar.DATE, -1)
                            } else {
                                break
                            }
                        }
                    }
                }
                break
            }
        }
        return streak
    }

    fun updateDailyGoal(minutes: Int) {
        _dailyGoalMinutes.value = minutes
        sharedPrefs.edit().putInt("daily_goal_minutes", minutes).apply()
    }

    fun incrementWater() {
        val todayStr = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
        val newval = (_waterIntake.value + 1).coerceAtMost(12)
        _waterIntake.value = newval
        sharedPrefs.edit().putInt("water_$todayStr", newval).apply()
    }

    fun decrementWater() {
        val todayStr = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
        val newval = (_waterIntake.value - 1).coerceAtLeast(0)
        _waterIntake.value = newval
        sharedPrefs.edit().putInt("water_$todayStr", newval).apply()
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel()
) {
    val workouts by viewModel.workouts.collectAsState()
    val favoriteNames by viewModel.favoriteNames.collectAsState()
    val completedWorkouts by viewModel.completedWorkouts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val dailyGoalMinutes by viewModel.dailyGoalMinutes.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()

    val completedTodayCount = remember(completedWorkouts) {
        val sdf = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
        val todayStr = sdf.format(java.util.Date())
        completedWorkouts.count { sdf.format(java.util.Date(it.timestamp)) == todayStr }
    }

    val completedTodayMinutes = remember(completedWorkouts) {
        val sdf = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
        val todayStr = sdf.format(java.util.Date())
        completedWorkouts.filter { sdf.format(java.util.Date(it.timestamp)) == todayStr }.sumOf { it.durationMinutes }
    }

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            WorkoutList(
                workouts = workouts,
                favoriteNames = favoriteNames,
                completedCount = completedTodayCount,
                completedTodayMinutes = completedTodayMinutes,
                dailyGoalMinutes = dailyGoalMinutes,
                waterIntake = waterIntake,
                viewModel = viewModel,
                isLoading = isLoading,
                isError = isError,
                onRetry = { viewModel.loadWorkouts() },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                navController = navController
            )
        }
        composable("activity") {
            ActivityScreen(
                workouts = workouts,
                favoriteNames = favoriteNames,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("bmi") {
            BmiScreen(
                workouts = workouts,
                navController = navController
            )
        }
        composable("profile") {
            ProfileScreen(
                dailyGoalMinutes = dailyGoalMinutes,
                onUpdateGoal = { viewModel.updateDailyGoal(it) }
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
                    onToggleFavorite = { viewModel.toggleFavorite(workout) },
                    onWorkoutCompleted = { viewModel.markCompleted(workout) }
                )
            } else {
                DetailNotFound(navController = navController)
            }
        }
    }
}

@Composable
fun WaterTrackerCard(
    waterIntake: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDDE8CC)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pelacak Minum Air",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hidrasi tubuh Anda agar performa tetap maksimal harian.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$waterIntake / 8 Gelas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (waterIntake >= 8) {
                        Text(
                            text = "Kebutuhan air harian tercapai! 💧",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Text("-", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (i in 1..8) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (i <= waterIntake) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutList(
    workouts: List<Workout>,
    favoriteNames: Set<String>,
    completedCount: Int,
    completedTodayMinutes: Int,
    dailyGoalMinutes: Int,
    waterIntake: Int,
    viewModel: WorkoutViewModel,
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
            completedTodayMinutes = completedTodayMinutes,
            dailyGoalMinutes = dailyGoalMinutes,
            waterIntake = waterIntake,
            viewModel = viewModel,
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
    completedTodayMinutes: Int,
    dailyGoalMinutes: Int,
    waterIntake: Int,
    viewModel: WorkoutViewModel,
    onToggleFavorite: (Workout) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedCategory by remember(workouts) { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }
    val categories = remember(workouts) {
        listOf("Semua") + workouts.map { it.categoryLabel() }.distinct()
    }
    val filteredWorkouts = remember(workouts, selectedCategory, searchQuery) {
        workouts.filter {
            val matchesCategory = selectedCategory == "Semua" || it.categoryLabel() == selectedCategory
            val matchesSearch = it.nama.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
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
                completedMinutes = completedTodayMinutes,
                dailyGoalMinutes = dailyGoalMinutes
            )
        }
        item {
            WaterTrackerCard(
                waterIntake = waterIntake,
                onIncrement = { viewModel.incrementWater() },
                onDecrement = { viewModel.decrementWater() }
            )
        }
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari latihan...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
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
    completedMinutes: Int,
    dailyGoalMinutes: Int
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
                    value = "$completedCount",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Target",
                    value = "$completedMinutes/$dailyGoalMinutes mnt",
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
                progress = { if (dailyGoalMinutes > 0) (completedMinutes.toFloat() / dailyGoalMinutes).coerceAtMost(1f) else 0f },
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
    var showTimer by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(isTimerRunning, secondsLeft, showTimer) {
        if (showTimer && isTimerRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    LaunchedEffect(secondsLeft, showTimer) {
        if (showTimer && secondsLeft == 0) {
            try {
                val toneG = android.media.ToneGenerator(android.media.AudioManager.STREAM_ALARM, 100)
                toneG.startTone(android.media.ToneGenerator.TONE_CDMA_PIP, 150)
            } catch (_: Exception) {}
            try {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(300, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(300)
                }
            } catch (_: Exception) {}
            onWorkoutCompleted()
            snackbarHostState.showSnackbar("Latihan ${workout.nama} selesai! Kerja bagus!")
            showTimer = false
        }
    }

    if (showTimer) {
        Dialog(
            onDismissRequest = { showTimer = false },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = workout.nama,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Lakukan gerakan dengan benar dan teratur",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        val progress = secondsLeft.toFloat() / 30f
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 10.dp,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$secondsLeft",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "detik",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isTimerRunning = !isTimerRunning },
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            if (isTimerRunning) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.size(width = 6.dp, height = 18.dp).background(MaterialTheme.colorScheme.primary))
                                    Box(modifier = Modifier.size(width = 6.dp, height = 18.dp).background(MaterialTheme.colorScheme.primary))
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                secondsLeft = 30
                                isTimerRunning = true
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Reset",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showTimer = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Batal")
                        }
                        Button(
                            onClick = {
                                onWorkoutCompleted()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Latihan ${workout.nama} selesai! Kerja bagus!")
                                }
                                showTimer = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Lewati")
                        }
                    }
                }
            }
        }
    }

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
                secondsLeft = 30
                isTimerRunning = true
                showTimer = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mulai Latihan (30s Timer)")
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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Menyimpan progres...")
            } else {
                Text("Tandai Selesai Instan")
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

@Composable
fun ProfileScreen(
    dailyGoalMinutes: Int = 15,
    onUpdateGoal: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Profil Saya",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
        )

        Text(
            text = "Target Waktu Harian",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Target aktif saat ini: $dailyGoalMinutes menit/hari",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf(5, 10, 15, 20, 30, 45, 60)) { mins ->
                        FilterChip(
                            selected = dailyGoalMinutes == mins,
                            onClick = { onUpdateGoal(mins) },
                            label = { Text("$mins mnt") }
                        )
                    }
                }
            }
        }
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Revaldo Aja",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NPM: 2417052021",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Pemrograman Mobile SI 2026",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ProfileSettingItem(
                    icon = Icons.Filled.Settings,
                    title = "Pengaturan Akun"
                )
                ProfileSettingItem(
                    icon = Icons.Filled.Notifications,
                    title = "Notifikasi"
                )
                ProfileSettingItem(
                    icon = Icons.Filled.Lock,
                    title = "Privasi & Keamanan"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Lainnya",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ProfileSettingItem(
                    icon = Icons.Filled.Info,
                    title = "Bantuan & Dukungan"
                )
                ProfileSettingItem(
                    icon = Icons.Filled.ExitToApp,
                    title = "Keluar",
                    isDestructive = true
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileSettingItem(
    icon: ImageVector,
    title: String,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (!isDestructive) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun WeeklyProgressChart(completedWorkouts: List<CompletedWorkout>) {
    val sdfDay = java.text.SimpleDateFormat("E", java.util.Locale("id", "ID"))
    val sdfDate = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
    val last7Days = remember(completedWorkouts) {
        val list = mutableListOf<Pair<String, Int>>()
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DATE, -6)
        for (i in 0..6) {
            val dateStr = sdfDate.format(cal.time)
            val rawLabel = sdfDay.format(cal.time)
            val dayLabel = if (rawLabel.length > 3) rawLabel.substring(0, 3) else rawLabel
            val count = completedWorkouts.count { sdfDate.format(java.util.Date(it.timestamp)) == dateStr }
            list.add(Pair(dayLabel, count))
            cal.add(java.util.Calendar.DATE, 1)
        }
        list
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Aktivitas 7 Hari Terakhir",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                last7Days.forEach { (day, count) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        val maxCount = 5
                        val heightFactor = (count.toFloat() / maxCount).coerceIn(0.1f, 1f)
                        val barHeight = 80.dp * heightFactor
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (count > 0) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.outlineVariant
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityScreen(
    workouts: List<Workout>,
    favoriteNames: Set<String>,
    viewModel: WorkoutViewModel,
    navController: NavController
) {
    val completedWorkouts by viewModel.completedWorkouts.collectAsState()
    val totalSessions = completedWorkouts.size
    val totalMinutes = completedWorkouts.sumOf { it.durationMinutes }
    val streak = remember(completedWorkouts) { viewModel.getWorkoutStreak(completedWorkouts) }
    val favoriteWorkouts = remember(workouts, favoriteNames) {
        workouts.filter { favoriteNames.contains(it.nama) }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Aktivitas Saya",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryCard(
                    title = "Total Sesi",
                    value = "$totalSessions",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Total Waktu",
                    value = "$totalMinutes mnt",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Streak",
                    value = "$streak hari",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            WeeklyProgressChart(completedWorkouts = completedWorkouts)
        }
        item {
            CategoryDonutChart(completedWorkouts = completedWorkouts)
        }
        item {
            Text(
                text = "Latihan Favorit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (favoriteWorkouts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Belum ada latihan favorit.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(favoriteWorkouts) { workout ->
                        WorkoutRowItem(
                            workout = workout,
                            isFavorite = true,
                            onToggleFavorite = { viewModel.toggleFavorite(workout) },
                            navController = navController
                        )
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Latihan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (completedWorkouts.isNotEmpty()) {
                    Text(
                        text = "Hapus Semua",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable { viewModel.clearHistory() }
                    )
                }
            }
        }
        if (completedWorkouts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada riwayat latihan. Mulai latihan hari ini!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(completedWorkouts.reversed()) { completed ->
                val dateFormatted = remember(completed.timestamp) {
                    val sdf = java.text.SimpleDateFormat("dd MMM, HH:mm", java.util.Locale("id", "ID"))
                    sdf.format(java.util.Date(completed.timestamp))
                }
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = completed.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${completed.category} • ${completed.durationMinutes} mnt",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = dateFormatted,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BmiScreen(
    workouts: List<Workout>,
    navController: NavController
) {
    var weight by remember { mutableStateOf(65f) }
    var height by remember { mutableStateOf(165f) }
    var bmiResult by remember { mutableStateOf<Float?>(null) }
    var bmiStatus by remember { mutableStateOf("") }
    var bmiAdvice by remember { mutableStateOf("") }
    var bmiColor by remember { mutableStateOf(CustomPrimary) }
    var recommendedWorkouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    val calculateBmi = {
        val heightInMeters = height / 100f
        val score = weight / (heightInMeters * heightInMeters)
        bmiResult = score
        when {
            score < 18.5f -> {
                bmiStatus = "Kurus"
                bmiAdvice = "Berat badan Anda kurang. Disarankan untuk menambah asupan kalori sehat dan fokus pada latihan kekuatan untuk membangun massa otot."
                bmiColor = Color(0xFFC87555)
                recommendedWorkouts = workouts.filter { 
                    val n = it.nama.lowercase()
                    n.contains("push") || n.contains("pull") || n.contains("squat")
                }
            }
            score >= 18.5f && score < 25f -> {
                bmiStatus = "Ideal"
                bmiAdvice = "Luar biasa! Berat badan Anda ideal. Pertahankan kondisi fisik Anda dengan kombinasi latihan kekuatan, core, dan fleksibilitas secara rutin."
                bmiColor = CustomPrimary
                recommendedWorkouts = workouts.filter {
                    val n = it.nama.lowercase()
                    n.contains("plank") || n.contains("sit") || n.contains("squat")
                }
            }
            score >= 25f && score < 30f -> {
                bmiStatus = "Berlebih"
                bmiAdvice = "Berat badan Anda berlebih. Cobalah kurangi asupan kalori berlebih dan tingkatkan aktivitas kardio serta latihan kekuatan tubuh."
                bmiColor = Color(0xFFD68A37)
                recommendedWorkouts = workouts.filter {
                    val n = it.nama.lowercase()
                    n.contains("mountain") || n.contains("lunge") || n.contains("plank")
                }
            }
            else -> {
                bmiStatus = "Obesitas"
                bmiAdvice = "Kategori Obesitas. Disarankan untuk berkonsultasi dengan ahli gizi dan memulai latihan fisik ringan secara konsisten demi kesehatan jantung Anda."
                bmiColor = Color(0xFFB3261E)
                recommendedWorkouts = workouts.filter {
                    val n = it.nama.lowercase()
                    n.contains("mountain") || n.contains("lunge") || n.contains("plank")
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Kalkulator BMI",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Berat Badan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${weight.toInt()} kg",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            IconButton(onClick = { weight = (weight - 1).coerceAtLeast(30f) }) {
                                Text("-", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { weight = (weight + 1).coerceAtMost(150f) }) {
                                Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    androidx.compose.material3.Slider(
                        value = weight,
                        onValueChange = { weight = it },
                        valueRange = 30f..150f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tinggi Badan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${height.toInt()} cm",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            IconButton(onClick = { height = (height - 1).coerceAtLeast(100f) }) {
                                Text("-", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { height = (height + 1).coerceAtMost(220f) }) {
                                Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    androidx.compose.material3.Slider(
                        value = height,
                        onValueChange = { height = it },
                        valueRange = 100f..220f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            Button(
                onClick = calculateBmi,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Hitung BMI", style = MaterialTheme.typography.titleMedium)
            }
        }
        bmiResult?.let { score ->
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, bmiColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Skor BMI Anda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format(java.util.Locale.US, "%.1f", score),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = bmiColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Surface(
                            color = bmiColor,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = bmiStatus,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                        Text(
                            text = bmiAdvice,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            if (recommendedWorkouts.isNotEmpty()) {
                item {
                    Text(
                        text = "Rekomendasi Latihan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(recommendedWorkouts) { workout ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .width(150.dp)
                                    .clickable { navController.navigate("detail/${Uri.encode(workout.nama)}") }
                            ) {
                                Column {
                                    WorkoutImage(
                                        workout = workout,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(90.dp)
                                    )
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = workout.nama,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${workout.durationValue()} mnt • ${workout.levelLabel()}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDonutChart(completedWorkouts: List<CompletedWorkout>) {
    val categoryCounts = remember(completedWorkouts) {
        val map = mutableMapOf<String, Int>()
        completedWorkouts.forEach {
            map[it.category] = (map[it.category] ?: 0) + 1
        }
        map
    }
    val total = remember(categoryCounts) { categoryCounts.values.sum().toFloat() }
    val colors = listOf(
        CustomPrimary,
        Color(0xFF99AD7A),
        Color(0xFFC87555),
        Color(0xFFD68A37)
    )
    val categories = listOf("Tubuh Atas", "Core", "Kaki", "Full Body")
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Proporsi Kategori Latihan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (total == 0f) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada data latihan untuk dianalisis.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.size(100.dp)
                    ) {
                        var startAngle = -90f
                        categories.forEachIndexed { index, cat ->
                            val count = categoryCounts[cat] ?: 0
                            if (count > 0) {
                                val sweepAngle = (count.toFloat() / total) * 360f
                                drawArc(
                                    color = colors[index % colors.size],
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 24f)
                                )
                                startAngle += sweepAngle
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        categories.forEachIndexed { index, cat ->
                            val count = categoryCounts[cat] ?: 0
                            val pct = if (total > 0f) (count.toFloat() / total * 100).toInt() else 0
                            if (count > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(colors[index % colors.size])
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$cat: $count ($pct%)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
