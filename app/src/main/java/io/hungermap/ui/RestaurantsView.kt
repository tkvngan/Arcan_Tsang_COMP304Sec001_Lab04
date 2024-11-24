package io.hungermap.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.size.Size
import io.hungermap.domain.Restaurant
import io.hungermap.interfaces.RestaurantSearchService
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Represents a view that displays a list of restaurants of the selected cuisine type.
 * @param cuisineType The cuisine type selected by the user.
 * @receiver Navigable The Navigable instance that is used to navigate between views.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.RestaurantsView(service: RestaurantSearchService, cuisineType: String) {
    val coroutineScope = rememberCoroutineScope()
    var restaurants by remember { mutableStateOf(emptyList<Restaurant>()) }
    var waitingForRestaurants by remember { mutableStateOf(true) }

    suspend fun reloadRestaurants() {
        waitingForRestaurants = true
        restaurants = service.searchRestaurants(keywords = listOf(cuisineType, "restaurant"))
        waitingForRestaurants = false
    }

    LaunchedEffect(cuisineType) {
        reloadRestaurants()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = cuisineType,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                modifier = Modifier.fillMaxWidth().shadow(8.dp),
                navigationIcon = {
                    IconButton(onClick = this::goHome) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                reloadRestaurants()
                            }
                        },
                        enabled = !waitingForRestaurants
                    ) { Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (waitingForRestaurants) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                return@Column
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(restaurants) { restaurant ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                showMapView(restaurant.name, restaurant.location)
                            },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(restaurant.name,
                                modifier = Modifier.fillMaxWidth(0.75f).padding(end = 4.dp),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            RatingBar(
                                modifier = Modifier.fillMaxWidth(1f).height(32.dp),
                                rating = restaurant.rating?.toFloat() ?: 0f,
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            val photoUri = restaurant.photoUri
                            if (photoUri != null) {
                                AsyncImage(
                                    modifier = Modifier.width(64.dp).height(64.dp),
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photoUri)
                                        .size(Size.ORIGINAL)
                                        .crossfade(true).allowHardware(true)
                                        .build(),
                                    contentDescription = null,
                                )
                            } else {
                                val colorIndex = abs(restaurant.name.hashCode()) % colorSet.size
                                Box(
                                    modifier = Modifier.width(64.dp).height(64.dp).background(colorSet[colorIndex])
                                ) {
                                    Text(text = restaurant.name.take(1).uppercase(),
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(restaurant.address ?: "",
                                    modifier = Modifier.fillMaxWidth(1f).padding(start = 16.dp),
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 20.sp
                                )
                                if (restaurant.telephone != null) {
                                    Text(
                                        restaurant.telephone,
                                        modifier = Modifier.fillMaxWidth(1f).padding(start = 16.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
) {
    val totalCount = 5
    Box(modifier = modifier.fillMaxHeight()) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalCount) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star",
                    modifier = Modifier.fillMaxWidth(1f / (totalCount - index))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1 - (rating / totalCount))
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .background(CardDefaults.cardColors().containerColor.copy(alpha = 0.8f))
        ) {}
    }
}

private val colorSet = listOf(
    Color(115, 81, 58),
    Color(194, 150, 130),
    Color(98, 122, 157),
    Color(87, 108, 67),
    Color(133, 128, 177),
    Color(103, 189, 170),
    Color(214, 126, 44),
    Color(80, 91, 166),
    Color(193, 90, 99),
    Color(94, 60, 108),
    Color(157, 188, 64),
    Color(224, 163, 46),
    Color(56, 61, 150),
    Color(70, 148, 73),
    Color(175, 54, 60),
    Color(231, 199, 31),
    Color(187, 86, 149),
    Color(8, 133, 161),
    Color(123, 45, 88),
    Color(66, 121, 119),
)
