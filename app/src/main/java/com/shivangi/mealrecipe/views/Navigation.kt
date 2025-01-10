// File: com/shivangi/mealrecipe/views/Navigation.kt

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navHostController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.RandomMealScreen.route
    ) {
        composable(Screen.RandomMealScreen.route) {
            RandomMealScreen(modifier = Modifier.padding(innerPadding))
        }
        composable(Screen.FavoriteScreen.route) {
            FavoriteScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
