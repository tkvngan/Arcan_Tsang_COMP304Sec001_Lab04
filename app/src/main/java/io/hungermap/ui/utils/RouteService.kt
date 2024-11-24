package io.hungermap.ui.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteService(apiKey: String) {
    private val geoContext = GeoApiContext.Builder()
        .apiKey(apiKey)
        .build()

    suspend fun getRoute(
        origin: LatLng,
        destination: LatLng,
        travelMode: TravelMode = TravelMode.DRIVING
    ): List<LatLng> = withContext(Dispatchers.IO) {
        val result = DirectionsApi.newRequest(geoContext)
            .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
            .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
            .mode(travelMode)
            .await()

        result.routes.firstOrNull()?.legs?.firstOrNull()?.steps?.flatMap { step ->
            step.polyline.decodePath().map { LatLng(it.lat, it.lng) }
        } ?: emptyList()
    }
}