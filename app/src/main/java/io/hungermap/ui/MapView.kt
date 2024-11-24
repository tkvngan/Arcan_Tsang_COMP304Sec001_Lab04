package io.hungermap.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationOff
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
import io.hungermap.ui.hooks.LocationTracker
import io.hungermap.ui.services.GeofencingService
import io.hungermap.ui.utils.RouteService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.MapView(
    restaurantName: String,
    location: Location
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationTracker = remember { LocationTracker(context) }
    val geofencingService = remember { GeofencingService(context) }
    val routeService = remember { RouteService("your_api_key_here") }

    // States
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var showGeofence by remember { mutableStateOf(true) }

    val restaurantLocation = LatLng(location.latitude, location.longitude)

    // Camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
    }

    // Location tracking
    LaunchedEffect(Unit) {
        if (locationTracker.hasLocationPermission()) {
            locationTracker.getLocationUpdates().collect { location ->
                userLocation = LatLng(location.latitude, location.longitude)
                // Update route when user location changes
                userLocation?.let { userLoc ->
                    routePoints = routeService.getRoute(userLoc, restaurantLocation)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurantName) },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showGeofence = !showGeofence }
                    ) {
                        Icon(
                            imageVector = if (showGeofence) Icons.Default.LocationOn
                            else Icons.Default.LocationOff,
                            contentDescription = "Toggle Geofence"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationTracker.hasLocationPermission(),
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true,
                    mapToolbarEnabled = true
                )
            ) {
                // Restaurant marker
                Marker(
                    state = MarkerState(position = restaurantLocation),
                    title = restaurantName,
                    snippet = "Tap for details"
                )

                // Geofence circle
                if (showGeofence) {
                    Circle(
                        center = restaurantLocation,
                        radius = 100.0, // 100 meters
                        fillColor = Color.Blue.copy(alpha = 0.1f),
                        strokeColor = Color.Blue,
                        strokeWidth = 2f
                    )
                }

                // Route polyline
                if (routePoints.isNotEmpty()) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 5f
                    )
                }
            }

            // Controls
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            userLocation?.let { userLoc ->
                                routePoints = routeService.getRoute(userLoc, restaurantLocation)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Get Route"
                    )
                }
            }
        }
    }
}