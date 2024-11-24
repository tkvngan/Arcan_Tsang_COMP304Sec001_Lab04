package io.hungermap.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import io.hungermap.domain.Location
import kotlinx.coroutines.launch
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.MapView(
    restaurantName: String,
    location: Location
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isMapLoaded by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    val restaurantLocation = remember(location) {
        LatLng(location.latitude, location.longitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
    }

    // Initialize GeoApiContext for routing
    val geoApiContext = remember {
        GeoApiContext.Builder()
            .apiKey("YOUR_API_KEY")
            .build()
    }

    // Function to calculate route
    suspend fun calculateRoute(origin: LatLng, destination: LatLng): List<LatLng> {
        return withContext(Dispatchers.IO) {
            try {
                val result = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                    .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                    .await()

                result.routes.firstOrNull()?.legs?.firstOrNull()?.steps?.flatMap { step ->
                    step.polyline.decodePath().map { LatLng(it.lat, it.lng) }
                } ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurantName) },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (userLocation != null) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val route = calculateRoute(userLocation!!, restaurantLocation)
                            routePoints = route
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Get Directions"
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Loading indicator
            if (!isMapLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    mapToolbarEnabled = true,
                    myLocationButtonEnabled = true
                ),
                onMapLoaded = {
                    isMapLoaded = true
                },
                onMyLocationClick = { location ->
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            ) {
                // Restaurant marker
                Marker(
                    state = MarkerState(position = restaurantLocation),
                    title = restaurantName,
                    snippet = "${location.latitude}, ${location.longitude}"
                )

                // Route polyline
                if (routePoints.isNotEmpty()) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 5f,
                        pattern = listOf(Dot(), Gap(8f))
                    )
                }
            }
        }
    }
}