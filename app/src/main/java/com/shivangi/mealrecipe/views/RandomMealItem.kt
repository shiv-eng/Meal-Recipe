package com.shivangi.mealrecipe.views

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.shivangi.mealrecipe.model.FavoriteMeal
import com.shivangi.mealrecipe.model.Meal
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun RandomMealItem(meal: Meal) {
    val favoritesVM: FavoritesViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(favoritesVM.allFavorites) {
        favoritesVM.allFavorites.value?.let { list ->
            isFavorite = list.any { it.mealId == meal.idMeal }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Meal Image + favorite
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .then(Modifier.background(MaterialTheme.colorScheme.surface)) // optional
        ) {
            // No corner clip
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color.Red else Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable {
                        scope.launch {
                            if (isFavorite) {
                                favoritesVM.removeFavorite(
                                    FavoriteMeal(
                                        mealId = meal.idMeal,
                                        name = meal.name,
                                        thumbnail = meal.thumbnail,
                                        origin = meal.origin,
                                        instructions = meal.instructions,
                                        ingredient = meal.getIngredients().joinToString(";") {
                                            "${it.ingredient}:${it.measurement}"
                                        },
                                        youtubeLink = meal.youtubeLink
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    "${meal.name} removed from favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                favoritesVM.addFavorite(
                                    FavoriteMeal(
                                        mealId = meal.idMeal,
                                        name = meal.name,
                                        thumbnail = meal.thumbnail,
                                        origin = meal.origin,
                                        instructions = meal.instructions,
                                        ingredient = meal.getIngredients().joinToString(";") {
                                            "${it.ingredient}:${it.measurement}"
                                        },
                                        youtubeLink = meal.youtubeLink
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    "${meal.name} added to favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            isFavorite = !isFavorite
                        }
                    }
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(
            text = meal.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Origin: ${meal.origin}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(Modifier.height(16.dp))

        Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Ingredients(meal.getIngredients())
        Spacer(Modifier.height(16.dp))

        Divider(thickness = 1.dp)
        Spacer(Modifier.height(16.dp))

        Text("Instructions:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        InstructionList(meal.instructions)
        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (!meal.youtubeLink.isNullOrBlank()) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtubeLink))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Preview")
                }
            }
            Button(onClick = {
                val content = "${meal.name}\n${meal.instructions}"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, content)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }) {
                Icon(Icons.Filled.Share, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Share")
            }
        }
    }
}
