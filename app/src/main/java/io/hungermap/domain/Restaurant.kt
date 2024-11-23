package io.hungermap.domain

/**
 * Represents a restaurant.
 */
data class Restaurant(
    val location: Location,
    val name: String,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val telephone: String? = null,
    val rating: Float? = null,
    val webSite: String? = null,
)
