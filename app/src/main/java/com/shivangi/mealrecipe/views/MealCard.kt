// File: com/shivangi.mealrecipe/views/MealCard.kt

package com.shivangi.mealrecipe.views

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
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
fun MealCard(meal: Meal, favoritesViewModel: FavoritesViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(favoritesViewModel.allFavorites) {
        favoritesViewModel.allFavorites.value?.let { favorites ->
            isFavorite = favorites.any { it.mealId == meal.idMeal }
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Meal Image with Favorite Icon Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = meal.thumbnail,
                    contentDescription = "Meal Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
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
                                            mealId = meal.idMeal,
                                            name = meal.name,
                                            thumbnail = meal.thumbnail,
                                            origin = meal.origin,
                                            instructions = meal.instructions,
                                            // Convert all ingredients into a single string if needed:
                                            ingredient = meal.getIngredients().joinToString("; ") {
                                                "${it.ingredient}:${it.measurement}"
                                            },
                                            youtubeLink = meal.youtubeLink
                                        )
                                    )
                                } else {
                                    favoritesViewModel.addFavorite(
                                        FavoriteMeal(
                                            mealId = meal.idMeal,
                                            name = meal.name,
                                            thumbnail = meal.thumbnail,
                                            origin = meal.origin,
                                            instructions = meal.instructions,
                                            ingredient = meal.getIngredients().joinToString("; ") {
                                                "${it.ingredient}:${it.measurement}"
                                            },
                                            youtubeLink = meal.youtubeLink
                                        )
                                    )
                                }
                                isFavorite = !isFavorite
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Meal Name
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Expand/Collapse Button
            var expanded by remember { mutableStateOf(false) }
            TextButton(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) "Hide Details" else "Show Details",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Ingredients Section
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Calls Meal's getIngredients() -> List<IngredientWithMeasurement>.
                Ingredients(meal.getIngredients())

                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Instructions Section
                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                InstructionList(meal.instructions)

                Spacer(modifier = Modifier.height(12.dp))

                // Share Button
                Button(
                    onClick = {
                        val contentToShare = "${meal.name}\n\n${meal.instructions}"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, contentToShare)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}
