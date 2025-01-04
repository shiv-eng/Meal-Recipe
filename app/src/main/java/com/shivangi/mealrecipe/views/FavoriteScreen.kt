package com.shivangi.mealrecipe.views

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    // For demonstration, just a placeholder screen.
    // You can add your list of favorites or logic as needed.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Favorite Meals",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
