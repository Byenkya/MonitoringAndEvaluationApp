import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Observer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.convertDateToPostgresFormat
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.data.GeometryUtils
import com.example.monitoringandevaluationapp.data.GeometryUtils.byteArrayToHexString
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.UserLocation
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Asset
import com.example.monitoringandevaluationapp.data.repository.PostProjectAssetRepository
import com.example.monitoringandevaluationapp.data.repository.PostProjectRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostAssetUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PDMViewModel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DateFieldWithPicker
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.ImageCaptureButton
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.captureImage
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.createImageFile
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.saveFileToDownloads
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PdmAssetsTab(pdmViewModel: PDMViewModel, navController: NavController, groupViewModel: GroupViewModel) {
    val groups = remember { mutableStateListOf<GroupEntity>() }
    var selectedGroup by remember { mutableStateOf("") }
    var selectedProjectGroups by remember { mutableStateOf<GroupEntity?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var id by remember { mutableStateOf("") }
    val uuid = remember { UUID.randomUUID().toString() }
    var groupName by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }
    var latX by remember { mutableStateOf("") }
    var lonY by remember { mutableStateOf("") }
    var assetId by remember { mutableStateOf("") }
    val dateAcquired = remember { mutableStateOf("") }
    var assetName by remember { mutableStateOf("") }
    var personInCharge by remember { mutableStateOf("") }
    var assetDescription by remember { mutableStateOf("") }
    val assetPhotoOneUri by pdmViewModel.assetPhotoOneUri.collectAsState()
    val assetPhotoTwoUri by pdmViewModel.assetPhotoTwoUri.collectAsState()
    var assetPhoto1 by remember { mutableStateOf("") }
    var assetPhoto2 by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    var dateCreated by remember { mutableStateOf<Date?>(null) }
    var updatedBy by remember { mutableStateOf("") }
    var dateUpdated by remember { mutableStateOf<Date?>(null) }
    val context = LocalContext.current
    // Declare request code for location permission
    val WRITE_REQUEST_CODE = 1003
    val READ_REQUEST_CODE = 1004
    val CAMERA_REQUEST_CODE = 1005

    val hasWritePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasWritePermission) {
        // Request write permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_REQUEST_CODE
        )
    }

    val hasReadPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasReadPermission) {
        // Request read permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_REQUEST_CODE
        )
    }

    val hasCameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasCameraPermission) {
        // Request camera permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )

    val cameraLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        pdmViewModel.updateAssetPhotoOneUri(uri)
    }

    val cameraLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        pdmViewModel.updateAssetPhotoTwoUri(uri)
    }

    LaunchedEffect(key1 = groupViewModel.allgroups) {
        groupViewModel.allgroups.observeForever { newList ->
            groups.clear()
            groups.addAll(newList)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

    ) {
//        TextField(
//            value = id,
//            onValueChange = { newValue -> id = newValue.filter { it.isDigit() } },
//            label = { Text("ID") },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )

//        TextField(
//            value = groupName,
//            onValueChange = { groupName = it },
//            label = { Text("Group Name") },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//        )

        // select group
        DropdownWithLabel(
            label = "Select Group",
            suggestions = groups.map { it.name }.distinct(),
            selectedText = selectedGroup,
            onTextSelected = { selectedName ->
                val selectedProjectGroup = groups.firstOrNull { it.name == selectedName }
                if (selectedProjectGroup != null) {
                    selectedGroup = selectedName
                    groupName = selectedName
                    groupId = selectedProjectGroup.id.toString()
                }
            }
        )

//        TextField(
//            value = groupId,
//            onValueChange = { newValue -> groupId = newValue.filter { it.isDigit() } },
//            label = { Text("Group ID") },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )

//        androidx.compose.material3.Text(
//            text = "Lat X: ${UserLocation.lat}", fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//        )
//
//        androidx.compose.material3.Text(
//            text = "Long Y: ${UserLocation.long}", fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//        )

        TextField(
            value = assetId,
            onValueChange = { assetId = it },
            label = { Text("Asset ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        DateFieldWithPicker(
            label = "Date Acquired",
            dates = Dates,
            date = dateAcquired,
            onDateSelected = { date ->
                pdmViewModel.updateDateAcquired(date)
            }
        )

        TextField(
            value = assetName,
            onValueChange = { assetName = it },
            label = { Text("Asset Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        TextField(
            value = personInCharge,
            onValueChange = { personInCharge = it },
            label = { Text("Person in Charge") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        TextField(
            value = assetDescription,
            onValueChange = { assetDescription = it },
            label = { Text("Asset Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .padding(top = 8.dp)
        )

        ImageCapture(
            title = "Asset Photo 1",
            context = context,
            cameraLauncher = cameraLauncher1,
            uri = uri,
            pdmViewModel = pdmViewModel ,
            imageUri = assetPhotoOneUri,
            imageUriUpdater = { updatedUri -> pdmViewModel.updateAssetPhotoOneUri(updatedUri) }
        )

        ImageCapture(
            title = "Asset Photo 2",
            context = context,
            cameraLauncher = cameraLauncher2,
            uri = uri,
            pdmViewModel = pdmViewModel ,
            imageUri = assetPhotoTwoUri,
            imageUriUpdater = { updatedUri -> pdmViewModel.updateAssetPhotoTwoUri(updatedUri) }
        )


        TextField(
            value = createdBy,
            onValueChange = { createdBy = it },
            label = { Text("Created By") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {
//                if (id == "") {
//                    missingFields.add("Asset ID Missing")
//                }

//                if (geom == "") {
//                    missingFields.add("Geom Missing")
//                }

                if (groupName == "") {
                    missingFields.add("groupName Missing")
                }

                if (groupId == "") {
                    missingFields.add("groupId Missing")
                }

                if (assetId == "") {
                    missingFields.add("assetID Missing")
                }

                if (assetName == "") {
                    missingFields.add("assetName Missing")
                }

                if (personInCharge == "") {
                    missingFields.add("personInCharge Missing")
                }

                if (assetDescription == "") {
                    missingFields.add("assetDescription Missing")
                }

                if (assetDescription == "") {
                    missingFields.add("assetDescription Missing")
                }

                if(Dates.dateAcquired.isBlank()) {
                    missingFields.add("Date acquired Missing")
                }

                if(assetPhotoOneUri == null) {
                    missingFields.add("Asset Photo 1 Missing")
                }

                if(assetPhotoTwoUri == null) {
                    missingFields.add("Asset Photo 2 Missing")
                }

                if (createdBy == "") {
                    missingFields.add("createdBy Missing")
                }

                if (missingFields.isNotEmpty()) {
                    showDialog.value = true
                    val missingFieldsMessage = "Please provide information for the following fields:\n${missingFields.joinToString(",\n")}"
                    errorMessage.value = missingFieldsMessage
                } else {
                    try {
                        // Get the current date and time
                        val currentDate = Date()

                        // Define a date format
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        // Format the current date and time into a string
                        val formattedDate = dateFormat.format(currentDate)

                        val assetPhotoOne = saveFileToDownloads(context, assetPhotoOneUri!!)
                        val assetPhotoTwo = saveFileToDownloads(context, assetPhotoTwoUri!!)
                        val geom = byteArrayToHexString(
                            GeometryUtils.createGeomFromLatLng(UserLocation.lat, UserLocation.long)
                        )

                        val asset = Asset(
                            geom = geom,
                            uuid = uuid,
                            group_name = groupName,
                            group_id = groupId.toDouble(),
                            lat_x = UserLocation.long,
                            lon_y = UserLocation.lat,
                            asset_id = assetId,
                            date_acquired = convertDateToPostgresFormat(Dates.dateAcquired),
                            asset_name = assetName,
                            person_incharge = personInCharge,
                            asset_description = assetDescription,
                            asset_photo1 = assetPhotoOne,
                            asset_photo2 = assetPhotoTwo,
                            created_by = createdBy,
                            date_created = formattedDate,
                            updated_by = formattedDate,
                            date_updated = formattedDate
                        )

                        val postProjectAssetRepository = PostProjectAssetRepository(RetrofitClient.apiService)
                        val fetchAndPostAssetUseCaseImpl = FetchAndPostAssetUseCaseImpl(asset, postProjectAssetRepository)
                        scope.launch {
                            isUploading = true
                            apiResponse = fetchAndPostAssetUseCaseImpl.execute()
                            if (apiResponse.message == "Asset saved successfully!!") {
                                id = ""
                                groupName = ""
                                groupId = ""
                                assetId = "0"
                                Dates.dateAcquired = ""
                                assetName = ""
                                personInCharge = ""
                                assetDescription = ""
                                pdmViewModel.updateAssetPhotoOneUri(null)
                                pdmViewModel.updateAssetPhotoTwoUri(null)
                                createdBy = ""
                                isUploading = false
                                Toast.makeText(
                                    context,
                                    "Message: ${apiResponse.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate("mapView")
                            } else {
                                isUploading = false
                                Toast.makeText(context, "Error: ${apiResponse.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch(e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("Asset Capture", "saving data failed due to ${e.message}")
                        throw RuntimeException("Capture: ${e.message}")
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Text("Submit")
        }

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }

        LoadingDialog(
            isLoading = isUploading,
            msg = "Uploading Asset...",
            onDismiss = { isUploading = false}
        )
    }
}

@Composable
fun DatePicker(
    selectedDate: Date?,
    onDateSelected: (Date?) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate?.toString() ?: "",
            onValueChange = { },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = label,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                }
            }
        )

        if (showDialog) {
            DateSelectorDialog(selectedDate = selectedDate) {
                onDateSelected(it)
                showDialog = false
            }
        }
    }
}

@Composable
fun DateSelectorDialog(
    selectedDate: Date?,
    onDateSelected: (Date?) -> Unit
) {
    // Implementation of the date picker dialog
    // This can vary based on the platform (Android, Desktop, Web)
    // You can use third-party libraries or platform-specific APIs
}

fun captureAssetImage(
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    pdmViewModel: PDMViewModel,
    imageUriUpdater: (Uri) -> Unit
) {
    try {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            val isCameraAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            if (isCameraAvailable) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(
                    context,
                    "Camera being used by another program",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    } catch (e: Exception) {
        Log.e("Launch Camera", "Starting camera failed due to ${e.message}")
    }
}


@Composable
fun ImageCapture(
    title: String,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    pdmViewModel: PDMViewModel,
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    imageUriUpdater: (Uri) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            captureAssetImage(context, cameraLauncher, uri, pdmViewModel, imageUriUpdater)
        }
    }


    if (imageUri?.path?.isNotEmpty() == true) {
        Image(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.Gray),
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = null
        )
    } else {
        Image(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.Gray),
            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
            contentDescription = null
        )
    }

    androidx.compose.material3.Button(
        onClick = {
            captureAssetImage(context, cameraLauncher, uri, pdmViewModel, imageUriUpdater)
        },
        modifier = modifier.fillMaxWidth()
    ) {
        androidx.compose.material3.Text("Capture $title")
    }
}




