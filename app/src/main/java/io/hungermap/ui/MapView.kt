package io.hungermap.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import io.hungermap.domain.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.MapView(
    restaurantName: String,
    location: Location
) {
    // Convert location to LatLng for Google Maps
    val restaurantLocation = LatLng(location.latitude, location.longitude)

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
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
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    mapToolbarEnabled = true
                )
            ) {
                // Add marker for restaurant location
                Marker(
                    state = MarkerState(position = restaurantLocation),
                    title = restaurantName,
                    snippet = "${location.latitude}, ${location.longitude}"
                )

                // Add circle to highlight area
                Circle(
                    center = restaurantLocation,
                    radius = 100.0, // 100 meters radius
                    fillColor = Color.Blue.copy(alpha = 0.1f),
                    strokeColor = Color.Blue,
                    strokeWidth = 2f
                )
            }
        }
    }
}