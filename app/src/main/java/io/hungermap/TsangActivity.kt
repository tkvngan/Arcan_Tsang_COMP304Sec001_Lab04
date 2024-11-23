package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.domain.Location
import io.hungermap.ui.MapView
import io.hungermap.ui.theme.HungerMapTheme

/**
 * This activity activates the MapView to show the location of a selected restaurant
 * on a map using Google Maps.
 */
class TsangActivity : NavigableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HungerMapTheme {
                MapView(
                    restaurantName = intent.getStringExtra("restaurantName")!!,
                    location = Location(
                        latitude = intent.getDoubleExtra("restaurantLatitude", 0.0),
                        longitude = intent.getDoubleExtra("restaurantLongitude", 0.0)
                    )
                )
            }
        }
    }
}