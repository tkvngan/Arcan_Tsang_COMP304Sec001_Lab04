package io.hungermap.ui

import io.hungermap.domain.Location

/**
 * An abstraction for navigating between different views in the HungerMap app.
 */
interface Navigable {

    /**
     * Navigates back to the previous view.
     */
    fun goBack()

    /**
     * Navigates to the home view.
     */
    fun goHome()

    /**
     * Navigates to the restaurants view for the specified cuisine type.
     */
    fun showRestaurantsView(cuisineType: String)

    /**
     * Navigates to the map view for the specified restaurant.
     */
    fun showMapView(restaurantName: String, location: Location)
}