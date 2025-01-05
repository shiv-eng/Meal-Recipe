package com.shivangi.mealrecipe.views

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivangi.mealrecipe.viewModels.RandomMealViewModel

@Composable
fun RandomMealScreen(modifier: Modifier = Modifier) {
    val randomMealViewModel: RandomMealViewModel = viewModel()
    val randomMealState by randomMealViewModel.randomMeal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        when {
            randomMealState.loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Simmering flavors, one pixel at a time...",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            randomMealState.error != null -> {
                Text(
                    text = randomMealState.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {
                randomMealState.meal?.let { RandomMealItem(it) }
            }
        }
    }
}
