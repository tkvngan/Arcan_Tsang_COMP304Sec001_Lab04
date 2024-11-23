package io.hungermap.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.hungermap.domain.Location

@Composable
fun Navigable.MapView(restaurantName: String, location: Location) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Map View", style = MaterialTheme.typography.displayMedium)
            HorizontalDivider()
            Text(restaurantName, style = MaterialTheme.typography.displaySmall)
            Text("${location.latitude}, ${location.longitude}", style = MaterialTheme.typography.displaySmall)
            Button(onClick = { goBack() }) {
                Text("Go Back")
            }
            Button(onClick = { goHome() }) {
                Text("Go Home")
            }
        }
    }
}