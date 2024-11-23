package io.hungermap.interfaces

import io.hungermap.domain.Restaurant

/**
 * Service for searching restaurants.
 */
interface RestaurantSearchService {

    /**
     * Searches for restaurants of the given type with the given keywords.
     */
    suspend fun searchRestaurants(type: String, vararg keywords: String): List<Restaurant>
}