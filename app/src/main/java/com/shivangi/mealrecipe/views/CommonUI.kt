package com.shivangi.mealrecipe.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shivangi.mealrecipe.model.IngredientWithMeasurement
import com.shivangi.mealrecipe.model.Meal

@Composable
fun BoxText(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun MealList(meals: List<Meal>) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(meals) { meal ->
            MealCard(meal, context)
            Divider(thickness = 1.dp)
        }
    }
}

@Composable
fun MealCard(meal: Meal, context: Context) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Meal image
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = "Meal thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Expand/collapse instructions
            TextButton(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) "Hide Details" else "Show Details",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (expanded) {
                // Ingredients
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Ingredients(meal.getIngredients())

                Spacer(modifier = Modifier.height(4.dp))

                // Instructions
                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = meal.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Share Button
                Button(
                    onClick = {
                        val contentToShare = "${meal.name}\n\n${meal.instructions}"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, contentToShare)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}

@Composable
fun Ingredients(ingredients: List<IngredientWithMeasurement>) {
    Column {
        ingredients.forEach { item ->
            Text(
                text = "${item.ingredient}: ${item.measurement}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RandomMealItem(meal: Meal) {
    val context = LocalContext.current

    // Simple scrollable container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Meal image
        AsyncImage(
            model = meal.thumbnail,
            contentDescription = "Random Meal thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Meal name
        Text(
            text = meal.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Origin
        Text(
            text = "Origin: " + (meal.origin ?: "Unknown"),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Ingredients
        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Ingredients(meal.getIngredients())

        Spacer(modifier = Modifier.height(12.dp))

        // Instructions
        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = meal.instructions,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // YouTube Link and Share
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (!meal.youtubeLink.isNullOrBlank()) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtubeLink))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Preview")
                }
            }

            Button(onClick = {
                val contentToShare = "${meal.name}\n${meal.instructions}"
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, contentToShare)
                }
                context.startActivity(Intent.createChooser(intent, null))
            }) {
                Icon(Icons.Default.Share, contentDescription = "Share recipe")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share")
            }
        }
    }
}
