package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.ui.RestaurantsView
import io.hungermap.ui.theme.HungerMapTheme

/**
 * This activity activates the view to show the list of restaurants of
 * the selected cuisine type.
 */
class ArcanActivity : NavigableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HungerMapTheme {
                RestaurantsView(
                    cuisineType = intent.getStringExtra("cuisineType")!!
                )
            }
        }
    }
}