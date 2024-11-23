package io.hungermap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hungermap.ui.MainView
import io.hungermap.ui.theme.HungerMapTheme

/**
 * Main entry point for the HungerMap app which activate the MainView that
 * shows a list of cuisine types for the user to select.
 */
class MainActivity : NavigableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HungerMapTheme {
                MainView()
            }
        }
    }
}
