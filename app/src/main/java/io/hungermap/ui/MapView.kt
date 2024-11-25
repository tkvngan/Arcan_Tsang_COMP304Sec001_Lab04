package io.hungermap.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.compose.*
import com.google.maps.model.TravelMode
import io.hungermap.domain.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.*
import io.hungermap.domain.Location
import io.hungermap.ui.utils.RouteService
import kotlinx.coroutines.launch
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.MapView(
    restaurant: Restaurant,
) {
    val restaurantName = restaurant.name
    val location = restaurant.location
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val routeService = remember { RouteService(context) }

    var isMapLoaded by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    val restaurantLocation = remember(location) {
        LatLng(location.latitude, location.longitude)
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { lastLocation ->
                    userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    LaunchedEffect(Unit) {
        startLocationUpdates()
    }

    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            routeService.cleanUp()
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
    }

    // Start navigation
    fun startNavigation() {
        scope.launch {
            userLocation?.let { origin ->
                // Get route
                val newRoute = routeService.getRoute(
                    origin = origin,
                    destination = restaurantLocation
                )

                routePoints = newRoute

                // Zoom to show route
                if (newRoute.isNotEmpty()) {
                    val bounds = LatLngBounds.builder().apply {
                        include(origin)
                        include(restaurantLocation)
                        newRoute.forEach { include(it) }
                    }.build()

                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngBounds(bounds, 100),
                        500
                    )
                }
                isNavigating = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurantName) },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true,
                    compassEnabled = false,
                    mapToolbarEnabled = false
                ),
                onMapLoaded = { isMapLoaded = true }
            ) {
                // Restaurant marker
                Marker(
                    state = MarkerState(position = restaurantLocation),
                    title = restaurantName
                )

                // Route polyline
                if (routePoints.isNotEmpty()) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 8f,
                        pattern = listOf(Dot(), Gap(8f))
                    )
                }
            }

            // Navigation FAB
            FloatingActionButton(
                onClick = { startNavigation() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = if (isNavigating) "Stop Navigation" else "Start Navigation"
                )
            }

            // Show loading while calculating route
            if (!isMapLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Show distance and duration
            if (routePoints.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 96.dp)
                ) {
                    Text(
                        text = "Follow the blue line to $restaurantName",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}