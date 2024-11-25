package io.hungermap.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.hungermap.R

/**
 * Represents the main view of the application.
 * @receiver Navigable The Navigable instance that is used to navigate between views.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigable.MainView() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(8.dp),
                title = {
                    Text(
                        "Hunger Map",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(0.dp))
            }
            items(cuisines.entries.toList()) { (cuisineType, id) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                        .clickable {
                            showRestaurantsView(cuisineType)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(8.dp))
                        ,
                        painter = painterResource(id = id),
                        contentScale = ContentScale.Crop,
                        contentDescription = cuisineType,
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = cuisineType,
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

        }
    }
}

val cuisines = mapOf<String, Int>(
    "American" to R.drawable.american,
    "Chinese" to R.drawable.chinese,
    "French" to R.drawable.french,
    "Indian" to R.drawable.indian,
    "Italian" to R.drawable.italian,
    "Japanese" to R.drawable.japanese,
    "Mexican" to R.drawable.mexican,
    "Thai" to R.drawable.thai,
    "Mediterranean" to R.drawable.mediterranean,
    "Vietnamese" to R.drawable.vietnamese,
    "Korean" to R.drawable.korean,
    "Greek" to R.drawable.greek,
    "Canadian" to R.drawable.canadian,
    "Turkish" to R.drawable.turkish,
    "Arabic" to R.drawable.arabian
)
