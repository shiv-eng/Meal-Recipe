// ----------------------
// Ingredients.kt
// Provide centerText param for FavoriteMeal if needed
// ----------------------
package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.shivangi.mealrecipe.model.IngredientWithMeasurement

@Composable
fun Ingredients(
    ingredients: List<IngredientWithMeasurement>,
    centerText: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalAlignment = if (centerText) Alignment.CenterHorizontally else Alignment.Start
    ) {
        ingredients.forEach { item ->
            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                Text(
                    text = "• ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${item.ingredient}: ${item.measurement}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
