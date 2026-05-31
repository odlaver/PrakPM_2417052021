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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
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
                        if (currentRoute == "home" || currentRoute == "profile") {
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

class WorkoutViewModel : ViewModel() {
    private val repository = WorkoutRepository()

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _favoriteNames = MutableStateFlow<Set<String>>(emptySet())
    val favoriteNames: StateFlow<Set<String>> = _favoriteNames.asStateFlow()

    private val _completedNames = MutableStateFlow<Set<String>>(emptySet())
    val completedNames: StateFlow<Set<String>> = _completedNames.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    init {
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
        _favoriteNames.value = if (currentFavorites.contains(workout.nama)) {
            currentFavorites - workout.nama
        } else {
            currentFavorites + workout.nama
        }
    }

    fun markCompleted(workout: Workout) {
        _completedNames.value = _completedNames.value + workout.nama
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
    val completedNames by viewModel.completedNames.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            WorkoutList(
                workouts = workouts,
                favoriteNames = favoriteNames,
                completedCount = completedNames.size,
                isLoading = isLoading,
                isError = isError,
                onRetry = { viewModel.loadWorkouts() },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                navController = navController
            )
        }
        composable("profile") {
            ProfileScreen()
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
                planMinutes = todayPlan.sumOf { it.durationValue() }
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

@Composable
fun ProfileScreen() {
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
