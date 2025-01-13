package com.shivangi.mealrecipe.views

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shivangi.mealrecipe.model.FavoriteMeal
import com.shivangi.mealrecipe.model.Meal
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun MealCard(
    meal: Meal,
    favoritesViewModel: FavoritesViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(favoritesViewModel.allFavorites) {
        favoritesViewModel.allFavorites.value?.let { list ->
            isFavorite = list.any { it.mealId == meal.idMeal }
        }
    }

    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 250.dp)
            .padding(8.dp)
        .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = meal.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
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
                                    favoritesViewModel.addFavorite(
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
            Spacer(Modifier.height(8.dp))

            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Divider(thickness = 1.dp)
            Spacer(Modifier.height(4.dp))

            if (expanded) {
                Spacer(Modifier.height(4.dp))
                Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Ingredients(meal.getIngredients(), centerText = false)

                Spacer(Modifier.height(4.dp))
                Divider(thickness = 1.dp)
                Spacer(Modifier.height(4.dp))

                Text("Instructions:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                InstructionList(meal.instructions)

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        val content = "${meal.name}\n\n${meal.instructions}"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, content)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Share, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}
