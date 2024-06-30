package com.example.monitoringandevaluationapp

import RetrofitClient
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.monitoringandevaluationapp.data.AppDatabase
import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.repository.GroupRepository
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.CaptureImageScreen
import com.example.monitoringandevaluationapp.presentation.ui.MapView.MapViewScreen
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessment.ProjectAssessment
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.ListOfProjectAssessments
import com.example.monitoringandevaluationapp.presentation.ui.ProjectDetails.ProjectDetails
import com.example.monitoringandevaluationapp.presentation.ui.SavedData.SavedImageList
import com.example.monitoringandevaluationapp.presentation.ui.sigin.GoogleAuthUiClient
import com.example.monitoringandevaluationapp.presentation.ui.sigin.LoginScreen
import com.example.monitoringandevaluationapp.presentation.ui.sigin.SignInViewModel
import com.example.monitoringandevaluationapp.data.repository.LocationRepository
import com.example.monitoringandevaluationapp.data.repository.PdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.data.repository.PdmProjectLocalRepository
import com.example.monitoringandevaluationapp.data.repository.PostPdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.data.repository.PostProjectGroupRepository
import com.example.monitoringandevaluationapp.data.repository.PostProjectRepository
import com.example.monitoringandevaluationapp.data.repository.savedAssessmentRepository
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PDMViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PdmProjectLocalViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.SavedAssessmentViewModel
import com.example.monitoringandevaluationapp.presentation.ui.PdmInfo.PdmScreen
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.PdmProjectAssessments
import com.example.monitoringandevaluationapp.presentation.ui.ProjectDetails.PdmProjectDetails
import com.example.monitoringandevaluationapp.presentation.ui.SavedData.SavedPdmProjects
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
class MainActivity : ComponentActivity() {
    lateinit var locationViewModel: LocationViewModel
    lateinit var pdmViewModel: PDMViewModel
    lateinit var savedAssessmentViewModel: SavedAssessmentViewModel
    lateinit var groupViewModel: GroupViewModel
    lateinit var pdmProjectViewModel: PdmProjectLocalViewModel

    private val googleAuthUiClient by lazy {
        try {
            GoogleAuthUiClient(
                context = applicationContext,
                oneTapClient = Identity.getSignInClient(applicationContext)
            )
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing googleAuthUiClient: ", e)
            throw e  // Re-throw to maintain original crash behavior after logging.
        }
    }



    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize LocationDao and LocationRepository (this is pseudo-code)
        val locationDao = AppDatabase.getDatabase(this).locationDao()
        val locationRepository = LocationRepository(locationDao)
        val savedAssessmentDao = AppDatabase.getDatabase(this).savedAssessmentDao()
        val savedAssessmentRepository = savedAssessmentRepository(savedAssessmentDao)
        val groupDao = AppDatabase.getDatabase(this).groupDao()
        val pdmProjectAssessmentDao = AppDatabase.getDatabase(this).pdmProjectAssessmentDao()
        val projectAssessmentRepository = PdmProjectAssessmentRepository(pdmProjectAssessmentDao)
        val pdmProjectDao = AppDatabase.getDatabase(this).pdmProjectDao()
        val pdmProjectLocalRepository = PdmProjectLocalRepository(pdmProjectDao)
        val groupRepository = GroupRepository(groupDao)
        val postProjectGroupRepository = PostProjectGroupRepository(RetrofitClient.apiService)

        groupViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GroupViewModel(applicationContext, groupRepository, postProjectGroupRepository) as T
                }
            }
        )[GroupViewModel::class.java]

        // Initialize the ViewModel
        locationViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LocationViewModel(locationRepository) as T
                }
            }
        )[LocationViewModel::class.java]

        savedAssessmentViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SavedAssessmentViewModel(savedAssessmentRepository) as T
                }
            }
        )[SavedAssessmentViewModel::class.java]

        pdmViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PDMViewModel() as T
                }
            }
        )[PDMViewModel::class.java]


        pdmProjectViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PdmProjectLocalViewModel(pdmProjectLocalRepository, projectAssessmentRepository) as T
                }
            }
        )[PdmProjectLocalViewModel::class.java]

        setContent {
            val navController = rememberNavController()

            // Get the current route
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                bottomBar = {
                    // Conditionally show BottomNavigation if not on "signIn" route
                    if (currentRoute != "signIn") {
                        AppBottomNavigation(navController)
                    }
                }
            ) {
                AppNavigation(
                    applicationContext,
                    lifecycleScope,
                    googleAuthUiClient,
                    navController,
                    locationViewModel,
                    pdmViewModel,
                    savedAssessmentViewModel,
                    groupViewModel,
                    pdmProjectViewModel,
                    projectAssessmentRepository
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation (backgroundColor = Color(0xFF6200EA)) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
            label = { Text("Map View", color = Color.White, fontSize = 8.sp) },
            selected = currentRoute == "mapView",
            onClick = {
                if (currentRoute != "mapView") {
                    navController.navigate("mapView")
                }
            }
        )
//        BottomNavigationItem(
//            icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White) },
//            label = { Text("Capture", color = Color.White, fontSize = 8.sp) },
//            selected = currentRoute == "captureImage",
//            onClick = {
//                if (currentRoute != "captureImage") {
//                    navController.navigate("captureImage")
//                }
//            }
//        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) },
            label = { Text("Projects", color = Color.White, fontSize = 8.sp) },
            selected = currentRoute == "SavedProjects",
            onClick = {
                if (currentRoute != "SavedProjects") {
                    navController.navigate("SavedProjects")
                }
            }
        )

        BottomNavigationItem(
            selected = currentRoute == "projectAssessment",
            icon = { Icon(Icons.Default.List, contentDescription = null, tint = Color.White) },
            label = { Text("Assess", color = Color.White, fontSize = 8.sp) },
            onClick = {
                if (currentRoute != "projectAssessment") {
                    navController.navigate("projectAssessment")
                }
            }
        )

        BottomNavigationItem(
            selected = currentRoute == "pdmInfo",
            icon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) },
            label = { Text("Register", color = Color.White, fontSize = 8.sp) },
            onClick = {
                if (currentRoute != "pdmInfo") {
                    navController.navigate("pdmInfo")
                }
            }
        )
        // Add other BottomNavigationItems here
    }
}

@Composable
fun AppNavigation(
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavHostController,
    locationViewModel: LocationViewModel,
    pdmViewModel: PDMViewModel,
    savedAssessmentViewModel: SavedAssessmentViewModel,
    groupViewModel: GroupViewModel,
    pdmProjectLocalViewModel: PdmProjectLocalViewModel,
    pdmProjectAssessmentRepository: PdmProjectAssessmentRepository
) {
    NavHost(navController = navController, startDestination = "mapView") {

        composable("signIn") {

            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.getSignInResultFromIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }

                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "SignIn successfull!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LoginScreen(
                navController = navController,
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable("mapView") {
            MapViewScreen(navController = navController, pdmProjectLocalViewModel = pdmProjectLocalViewModel)
        }
        composable("captureImage") {
            CaptureImageScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable("SavedProjects") {
//            SavedImageList(navController = navController, viewModel = locationViewModel)
            SavedPdmProjects(navController = navController, viewModel = pdmProjectLocalViewModel)
        }
        composable("projectAssessments/{projectName}") {backStackEntry ->
            // Retrieve the projectName from the navigation arguments
            val projectName = backStackEntry.arguments?.getString("projectName")

            val projectAssessments = remember { mutableStateListOf<PdmProjectAssessmentEntity>() }

            LaunchedEffect(key1 = pdmProjectLocalViewModel.allProjectAssessments) {
                pdmProjectLocalViewModel.allProjectAssessments.observeForever { newList ->
                    projectAssessments.clear()
                    projectAssessments.addAll(newList)
                }
            }

            // Create an instance of PostProjectRepository (replace with your actual PostProjectRepository)
            val postProjectRepository = PostProjectRepository(RetrofitClient.apiService)
            val postPdmProjectAssessmentRepository = PostPdmProjectAssessmentRepository(RetrofitClient.apiService)

            // Pass the LocationEntity to the ProjectDetails composable
            if (!projectAssessments.isNullOrEmpty()) {
                val assessmentCount = projectAssessments.size
                PdmProjectAssessments(
                    navController = navController,
                    assessments = projectAssessments.filter { it.projectName == projectName },
                    pdmProjectAssessmentRepository = pdmProjectAssessmentRepository,
                    postPdmProjectAssessmentRepository = postPdmProjectAssessmentRepository
                )
//                ListOfProjectAssessments(
//                    navController = navController,
//                    assessments = projectAssessments,
//                    assessmentCount = assessmentCount,
//                    viewModel = locationViewModel,
//                    savedAssessmentViewModel = savedAssessmentViewModel,
//                    projectRepository = postProjectRepository
//                )
            }
        }

        composable("projectDetails/{locationId}") { backStackEntry ->
            val projectId = remember { mutableStateOf<String?>(null) }
            var pdmProjectEntity: PdmProjectEntity? by remember { mutableStateOf(null) }

            // Retrieve the projectId from the navigation arguments
            LaunchedEffect(backStackEntry.arguments?.getString("locationId")) {
                projectId.value = backStackEntry.arguments?.getString("locationId")
            }

            // Observe changes in the project entity based on the projectId
            LaunchedEffect(projectId.value) {
                projectId.value?.let { id ->
                    pdmProjectLocalViewModel.getPdmProjectById(id.toLong()).observeForever { entity ->
                        pdmProjectEntity = entity
                    }
                }
            }

            // Display the PdmProjectDetails when pdmProjectEntity is not null
            if (pdmProjectEntity != null) {
                PdmProjectDetails(navController = navController, pdmProjectEntity = pdmProjectEntity!!)
            }

        }

        composable("projectAssessment") {
            ProjectAssessment(
                navController = navController,
                projectPdmProjectLocationViewModel = pdmProjectLocalViewModel,
                groupViewModel = groupViewModel,
                pdmProjectAssessmentRepository = pdmProjectAssessmentRepository
            )
        }

        composable("pdmInfo") {
            PdmScreen(
                navController = navController,
                pdmViewModel = pdmViewModel,
                groupViewModel = groupViewModel,
                pdmProjectLocalViewModel = pdmProjectLocalViewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertDateToPostgresFormat(dateStr: String): String {
    // Parse the input date string with the given format
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(dateStr, formatter)

    // Format the date to the required PostgreSQL format
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}



