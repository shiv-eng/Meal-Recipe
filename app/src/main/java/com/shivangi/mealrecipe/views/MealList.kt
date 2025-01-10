// File: com/shivangi/mealrecipe/views/MealList.kt

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivangi.mealrecipe.model.Meal
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel

@Composable
fun MealList(meals: List<Meal>, favoritesViewModel: FavoritesViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(meals) { meal ->
            MealCard(meal, favoritesViewModel)
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        }
    }
}
