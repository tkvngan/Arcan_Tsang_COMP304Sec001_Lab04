package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.domain.Location
import io.hungermap.ui.MapView
import io.hungermap.ui.theme.HungerMapTheme
import android.util.Log
import io.hungermap.domain.Restaurant

class TsangActivity : NavigableActivity() {
    companion object {
        private const val TAG = "TsangActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        try {
            val restaurant = Restaurant(
                name = intent.getStringExtra("restaurant.name")!!,
                location = Location(
                    latitude = intent.getDoubleExtra("restaurant.location.latitude", 0.0),
                    longitude = intent.getDoubleExtra("restaurant.location.longitude", 0.0)
                ),
                address = intent.getStringExtra("restaurant.address"),
                city = intent.getStringExtra("restaurant.city"),
                country = intent.getStringExtra("restaurant.country"),
                telephone = intent.getStringExtra("restaurant.telephone"),
                rating = if (intent.hasExtra("restaurant.rating"))
                    intent.getDoubleExtra("restaurant.rating", 0.0) else null,
                website = intent.getStringExtra("restaurant.website"),
                type = intent.getStringExtra("restaurant.type"),
                photoUri = intent.getStringExtra("restaurant.photoUri")
            )
            setContent {
                HungerMapTheme {
                    MapView(restaurant)
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