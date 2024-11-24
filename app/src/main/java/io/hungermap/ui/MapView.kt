package io.hungermap.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant

@Composable
fun Navigable.MapView(restaurantName: String, location: Location) {
    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val restaurantLocation = LatLng(location.latitude, location.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
    }

    val mapProperties = remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                mapType = MapType.NORMAL,
            )
        )
    }

    val mapUiSetting by remember {
        mutableStateOf(
            MapUiSettings(
                isZoomControlsEnabled = true,
                isMyLocationButtonEnabled = true,
                isCompassEnabled = true,
            )
        )
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(restaurantName)
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = { goHome() }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Go Home")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties.value,
                uiSettings = mapUiSetting
            ) {
                Marker(
                    state = MarkerState(position = restaurantLocation),
                    title = restaurantName,
                    snippet = "Lat: ${location.latitude}, Long: ${location.longitude}"
                )
            }

            if (!hasLocationPermission) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Location Permission Required")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { requestPermission() }) {
                            Text("Grant Permission")
                        }
                    }
                }
            }
        }
    }
}