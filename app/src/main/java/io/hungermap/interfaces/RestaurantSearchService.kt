package io.hungermap.interfaces

import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant

/**
 * Service for searching restaurants.
 */
interface RestaurantSearchService {

    suspend fun getCurrentLocation(): Location

    /**
     * Searches for restaurants of the given type with the given keywords.
     */
    suspend fun searchRestaurants(location: Location? = null, radius: Double = 1000.0, keywords: List<String>): List<Restaurant>
}