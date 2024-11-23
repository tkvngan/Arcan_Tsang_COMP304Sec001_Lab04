package io.hungermap.interfaces

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import io.hungermap.domain.Location
import io.hungermap.domain.Restaurant
import kotlinx.coroutines.tasks.await

class RestaurantSearchServiceImpl(context: Context) : RestaurantSearchService {

    private val placesClient: PlacesClient = Places.createClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun searchRestaurants(type: String, vararg keywords: String): List<Restaurant> {
        // TODO: Need further elaboration on the implementation of this method.
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        val response = placesClient.findCurrentPlace(request).await()
        val places = response.placeLikelihoods.map { it.place }
        return places.map {
            Restaurant(
                location = Location(it.latLng!!.latitude, it.latLng!!.longitude),
                name = it.name!!,
                address = it.address
            )
        }
    }
}