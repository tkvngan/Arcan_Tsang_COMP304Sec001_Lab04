package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.google.android.libraries.places.api.Places
import io.hungermap.ui.MainView
import io.hungermap.ui.theme.HungerMapTheme

/**
 * Main entry point for the HungerMap app which activate the MainView that
 * shows a list of cuisine types for the user to select.
 * The actual logic for displaying the list of cuisine types is in the MainView class.
 */
class MainActivity : NavigableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey = getString(R.string.google_places_api_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .logger(DebugLogger())
                .build()
        }
        enableEdgeToEdge()
        setContent {
            HungerMapTheme {
                MainView()
            }
        }
    }
}
