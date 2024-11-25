package io.hungermap

import android.content.Intent
import androidx.activity.ComponentActivity
import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant
import io.hungermap.ui.Navigable

/**
 * An activity that can navigate between different views in the HungerMap app. It
 * provides implementations for the [Navigable] interface for an activity-based
 * navigation model.
 */
abstract class NavigableActivity : ComponentActivity(), Navigable {

    override fun goBack() {
        finish()
    }

    override fun goHome() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

    override fun showRestaurantsView(cuisineType: String) {
        val intent = Intent(this, ArcanActivity::class.java).apply {
            putExtra("cuisineType", cuisineType)
        }
        startActivity(intent)
    }

    override fun showMapView(restaurant: Restaurant) {
        val intent = Intent(this, TsangActivity::class.java).apply {
            putExtra("restaurant.name", restaurant.name)
            putExtra("restaurant.location.latitude", restaurant.location.latitude)
            putExtra("restaurant.location.longitude", restaurant.location.longitude)
            if (restaurant.address != null) {
                putExtra("restaurant.address", restaurant.address)
            }
            if (restaurant.city != null) {
                putExtra("restaurant.city", restaurant.city)
            }
            if (restaurant.country != null) {
                putExtra("restaurant.country", restaurant.country)
            }
            if (restaurant.telephone != null) {
                putExtra("restaurant.telephone", restaurant.telephone)
            }
            if (restaurant.rating != null) {
                putExtra("restaurant.rating", restaurant.rating)
            }
            if (restaurant.website != null) {
                putExtra("restaurant.website", restaurant.website)
            }
            if (restaurant.type != null) {
                putExtra("restaurant.type", restaurant.type)
            }
            if (restaurant.photoUri != null) {
                putExtra("restaurant.photoUri", restaurant.photoUri)
            }
        }
        startActivity(intent)
    }
}