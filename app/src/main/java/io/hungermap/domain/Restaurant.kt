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
    val rating: Double? = null,
    val website: String? = null,
    val type: String? = null,
    val photoUri: String? = null
)
