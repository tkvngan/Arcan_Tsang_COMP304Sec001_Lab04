package io.hungermap.ui.utils

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteService(context: Context) {
    private val geoContext = GeoApiContext.Builder()
        .apiKey("AIzaSyA-VvVEaEZqPJTqKuZK7kzcvD7jH-mj4as")
        .build()

    suspend fun getRoute(
        origin: LatLng,
        destination: LatLng,
        travelMode: TravelMode = TravelMode.DRIVING
    ): List<LatLng> = withContext(Dispatchers.IO) {
        try {
            val result = DirectionsApi.newRequest(geoContext)
                .mode(travelMode)
                .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .await()

            result.routes.firstOrNull()?.legs?.firstOrNull()?.steps?.flatMap { step ->
                step.polyline.decodePath().map { LatLng(it.lat, it.lng) }
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun cleanUp() {
        geoContext.shutdown()
    }
}