package io.hungermap.interfaces

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AuthorAttribution
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFindCurrentPlace
import com.google.android.libraries.places.api.net.kotlin.awaitSearchByText
import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant

class RestaurantSearchServiceImpl(context: Context) : RestaurantSearchService {

    private val placesClient: PlacesClient = Places.createClient(context)
    private lateinit var currentLocation: Location

    @SuppressLint("MissingPermission")
    private suspend fun initialize() {
        if (!this::currentLocation.isInitialized) {
            val response = placesClient.awaitFindCurrentPlace(listOf(Place.Field.LAT_LNG, Place.Field.FORMATTED_ADDRESS))
            val place = response.placeLikelihoods.firstOrNull()?.place
            currentLocation = if (place != null) {
                Location(place.location!!.latitude, place.location!!.longitude)
            } else {
                Location(43.65531413443091, -79.38847054670805) // default location = Toronto Downtown
            }
        }
    }

    override suspend fun getCurrentLocation(): Location {
        initialize()
        return currentLocation
    }

    override suspend fun searchRestaurants(location: Location?, radius: Double, keywords: List<String>): List<Restaurant> {
        initialize()
        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.LOCATION,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.NATIONAL_PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
            Place.Field.RATING,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.PRIMARY_TYPE,
            Place.Field.PRIMARY_TYPE_DISPLAY_NAME,
            Place.Field.PHOTO_METADATAS
        )
        val query = keywords.joinToString(" ")
        val response = placesClient.awaitSearchByText(query, placeFields) {
            val latLng = if (location != null) {
                LatLng(location.latitude, location.longitude)
            } else {
                LatLng(currentLocation.latitude, currentLocation.longitude)
            }
            locationBias = CircularBounds.newInstance(latLng, radius)
            includedType = "restaurant"
            isStrictTypeFiltering = true
        }
        return response.places.map { place ->
            val attributions: List<AuthorAttribution> = place.photoMetadatas.flatMap { meta ->
                meta.authorAttributions?.asList()?.filter { attribution ->
                    attribution.name == place.displayName
                } ?: emptyList()
            }
            val attribution = attributions.firstOrNull()
            Restaurant(
                location = Location(place.location!!.latitude, place.location!!.longitude),
                name = place.displayName!!,
                address = place.formattedAddress,
                telephone = place.nationalPhoneNumber,
                rating = place.rating,
                city = place.addressComponents?.asList()?.firstOrNull { place.placeTypes?.contains("locality") == true }?.name,
                country = place.addressComponents?.asList()?.firstOrNull { place.placeTypes?.contains("country") == true }?.name,
                website = place.websiteUri?.toString(),
                type = place.primaryTypeDisplayName,
                photoUri = attribution?.photoUri
            )
        }
    }
}