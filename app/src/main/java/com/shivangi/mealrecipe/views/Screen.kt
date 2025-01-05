package com.shivangi.mealrecipe.views

sealed class Screen(val route: String) {
    object RandomMealScreen : Screen("Random")
    object FavoriteScreen : Screen("Favorite")
}
