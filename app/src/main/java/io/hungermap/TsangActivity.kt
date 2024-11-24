package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.domain.Location
import io.hungermap.ui.MapView
import io.hungermap.ui.theme.HungerMapTheme

class TsangActivity : NavigableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get data from intent with default values (in case of null)
        val restaurantName = intent.getStringExtra("restaurantName") ?: "Restaurant"
        val latitude = intent.getDoubleExtra("restaurantLatitude", 37.4219999) // Default to Mountain View
        val longitude = intent.getDoubleExtra("restaurantLongitude", -122.0840575)

        setContent {
            HungerMapTheme {
                MapView(
                    restaurantName = restaurantName,
                    location = Location(
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            }
        }
    }
}