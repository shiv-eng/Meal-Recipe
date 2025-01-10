// File: com/shivangi.mealrecipe/views/Ingredients.kt

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivangi.mealrecipe.model.IngredientWithMeasurement

@Composable
fun Ingredients(ingredients: List<IngredientWithMeasurement>) {
    Column(modifier = Modifier.padding(start = 10.dp)) {
        ingredients.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
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
