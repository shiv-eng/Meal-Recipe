package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel

@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    val favoritesVM: FavoritesViewModel = viewModel()
    val favoriteMeals by favoritesVM.allFavorites.observeAsState(emptyList())

    if (favoriteMeals.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            BoxText("No favorite meals yet.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(favoriteMeals) { fav ->
                FavoriteMealCard(fav, favoritesVM)
            }
        }
    }
}
