package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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