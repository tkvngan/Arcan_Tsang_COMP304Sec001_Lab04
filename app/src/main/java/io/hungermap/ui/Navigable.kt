package io.hungermap.ui

import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant

/**
 * An abstraction for navigating between different views in the HungerMap app.
 * This interface is used to decouple the navigation logic from the view classes.
 *
 * There is an implementation of this interface in the NavigableActivity class that
 * uses the traditional activity-based navigation.
 *
 * An alternative implementation could be implemented using Jetpack Compose Navigation
 * Component.
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
    @Deprecated("Use showMapView(restaurant: Restaurant) instead.")
    fun showMapView(restaurantName: String, location: Location)

    /**
     * Navigates to the map view for the specified restaurant.
     */
    fun showMapView(restaurant: Restaurant)
}