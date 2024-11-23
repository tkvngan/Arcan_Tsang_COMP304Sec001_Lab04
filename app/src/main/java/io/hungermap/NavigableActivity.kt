package io.hungermap

import android.content.Intent
import androidx.activity.ComponentActivity
import io.hungermap.domain.Location
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

    override fun showMapView(restaurantName: String, location: Location) {
        val intent = Intent(this, TsangActivity::class.java).apply {
            putExtra("restaurantName", restaurantName)
            putExtra("restaurantLatitude", location.latitude)
            putExtra("restaurantLongitude", location.longitude)
        }
        this.startActivity(intent)
    }
}