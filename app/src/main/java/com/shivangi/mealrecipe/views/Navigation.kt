package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shivangi.mealrecipe.views.Screen.FavoriteScreen
import com.shivangi.mealrecipe.views.Screen.RandomMealScreen
import com.shivangi.mealrecipe.views.Screen.SearchMealScreen

@Composable
fun Navigation(navHostController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navHostController, startDestination = RandomMealScreen.route) {
        composable(route = RandomMealScreen.route) {
            RandomMealScreen(modifier = Modifier.padding(innerPadding))
        }
        composable(route = SearchMealScreen.route) {
            SearchMealScreen(modifier = Modifier.padding(innerPadding))
        }
        composable(route = FavoriteScreen.route) {
            FavoriteScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
