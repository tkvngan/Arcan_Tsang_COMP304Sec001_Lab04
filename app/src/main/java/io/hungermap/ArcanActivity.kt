package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.libraries.places.api.Places
import io.hungermap.interfaces.RestaurantSearchService
import io.hungermap.interfaces.RestaurantSearchServiceImpl
import io.hungermap.ui.RestaurantsView
import io.hungermap.ui.theme.HungerMapTheme

/**
 * This activity activates the view to show the list of restaurants of
 * the selected cuisine type.
 * This class is needed for the sake of the assignment instructions which
 * require the creation of an activity using the first name of the first
 * member of the group.
 * The actual logic for displaying the list of restaurants is in the
 * RestaurantsView class.
 */
class ArcanActivity : NavigableActivity() {
    lateinit var service: RestaurantSearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_places_api_key))
        }
        if (!this::service.isInitialized) {
            service = RestaurantSearchServiceImpl(applicationContext)
        }
        enableEdgeToEdge()
        setContent {
            HungerMapTheme {
                RestaurantsView(
                    service = service,
                    cuisineType = intent.getStringExtra("cuisineType")!!
                )
            }
        }
    }
}