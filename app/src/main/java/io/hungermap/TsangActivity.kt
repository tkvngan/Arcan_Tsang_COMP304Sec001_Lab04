package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.domain.Location
import io.hungermap.ui.MapView
import io.hungermap.ui.theme.HungerMapTheme
import android.util.Log

class TsangActivity : NavigableActivity() {
    companion object {
        private const val TAG = "TsangActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            val restaurantName = intent.getStringExtra("restaurantName")
                ?: throw IllegalArgumentException("Restaurant name is required")

            val latitude = intent.getDoubleExtra("restaurantLatitude", 0.0)
            val longitude = intent.getDoubleExtra("restaurantLongitude", 0.0)

            if (latitude == 0.0 || longitude == 0.0) {
                throw IllegalArgumentException("Invalid location coordinates")
            }

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
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing map view", e)
            finish() // Return to previous screen if initialization fails
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any resources if needed
    }
}