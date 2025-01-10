// File: com/shivangi.mealrecipe/views/RandomMealItem.kt

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shivangi.mealrecipe.model.FavoriteMeal
import com.shivangi.mealrecipe.model.Meal
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun RandomMealItem(meal: Meal) {
    val context = LocalContext.current
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(favoritesViewModel.allFavorites) {
        favoritesViewModel.allFavorites.value?.let { favorites ->
            isFavorite = favorites.any { it.mealId == meal.idMeal }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Meal Image with Favorite Icon Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = "Random Meal Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) Color.Red else Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable {
                        scope.launch {
                            if (isFavorite) {
                                favoritesViewModel.removeFavorite(
                                    FavoriteMeal(
                                        mealId = favoriteMeal.mealId,
                                        name = favoriteMeal.name,
                                        thumbnail = favoriteMeal.thumbnail,
                                        origin = favoriteMeal.origin,
                                        instructions = favoriteMeal.instructions,
                                        youtubeLink = favoriteMeal.youtubeLink
                                    )
                                )
                            } else {
                                favoritesViewModel.addFavorite(
                                    FavoriteMeal(
                                        mealId = favoriteMeal.mealId,
                                        name = favoriteMeal.name,
                                        thumbnail = favoriteMeal.thumbnail,
                                        origin = favoriteMeal.origin,
                                        instructions = favoriteMeal.instructions,
                                        youtubeLink = favoriteMeal.youtubeLink
                                    )
                                )
                            }
                            isFavorite = !isFavorite
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Meal Name
        Text(
            text = meal.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Origin
        Text(
            text = "Origin: ${meal.origin}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients Section
        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Ingredients(meal.getIngredients())
        Spacer(modifier = Modifier.height(16.dp))

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        // Instructions Section
        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        InstructionList(meal.instructions)
        Spacer(modifier = Modifier.height(24.dp))

        // YouTube Link and Share Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (!meal.youtubeLink.isNullOrBlank()) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtubeLink))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Preview")
                }
            }

            Button(onClick = {
                val contentToShare = "${meal.name}\n${meal.instructions}"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, contentToShare)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }) {
                Icon(Icons.Filled.Share, contentDescription = "Share Recipe")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share")
            }
        }
    }
}
