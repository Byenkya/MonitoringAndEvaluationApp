package com.example.monitoringandevaluationapp

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
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
import com.example.monitoringandevaluationapp.presentation.CaptureData.CaptureImageScreen
import com.example.monitoringandevaluationapp.presentation.MapView.MapViewScreen
import com.example.monitoringandevaluationapp.presentation.ProjectAssessment.ProjectAssessment
import com.example.monitoringandevaluationapp.presentation.ProjectDetails.ProjectDetails
import com.example.monitoringandevaluationapp.presentation.SavedData.SavedImageList
import com.example.monitoringandevaluationapp.presentation.sigin.GoogleAuthUiClient
import com.example.monitoringandevaluationapp.presentation.sigin.LoginScreen
import com.example.monitoringandevaluationapp.presentation.sigin.SignInViewModel
import com.example.monitoringandevaluationapp.repository.LocationRepository
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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

    lateinit var locationViewModel: LocationViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize LocationDao and LocationRepository (this is pseudo-code)
        val locationDao = AppDatabase.getDatabase(this).locationDao()
        val locationRepository = LocationRepository(locationDao)


        // Initialize the ViewModel
        locationViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LocationViewModel(locationRepository) as T
                }
            }
        )[LocationViewModel::class.java]

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
                    locationViewModel
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
            label = { Text("Map View", color = Color.White) },
            selected = currentRoute == "mapView",
            onClick = {
                if (currentRoute != "mapView") {
                    navController.navigate("mapView")
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White) },
            label = { Text("Capture", color = Color.White) },
            selected = currentRoute == "captureImage",
            onClick = {
                if (currentRoute != "captureImage") {
                    navController.navigate("captureImage")
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) },
            label = { Text("Projects", color = Color.White) },
            selected = currentRoute == "SavedImages",
            onClick = {
                if (currentRoute != "SavedImages") {
                    navController.navigate("SavedImages")
                }
            }
        )

        BottomNavigationItem(
            selected = currentRoute == "projectAssessment",
            icon = { Icon(Icons.Default.List, contentDescription = null, tint = Color.White) },
            label = { Text("Assess", color = Color.White) },
            onClick = {
                if (currentRoute != "projectAssessment") {
                    navController.navigate("projectAssessment")
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
            MapViewScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable("captureImage") {
            CaptureImageScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable("SavedImages") {
            SavedImageList(navController = navController, viewModel = locationViewModel)
        }
        composable("projectDetails/{locationId}") { backStackEntry ->
            // Retrieve the locationId from the navigation arguments
            val locationId = backStackEntry.arguments?.getString("locationId")
            // Retrieve the corresponding LocationEntity based on locationId
            val locationEntity = locationViewModel.allLocations.value?.firstOrNull {
                it.id == locationId?.toInt()
            }
            // Pass the LocationEntity to the ProjectDetails composable
            if (locationEntity != null) {
                ProjectDetails(navController = navController, locationEntity = locationEntity)
            }
        }

        composable("projectAssessment") {
            ProjectAssessment(navController = navController, locationViewModel = locationViewModel)
        }
    }
}



