// File: com/shivangi/mealrecipe/data/FavoritesRepository.kt

package com.shivangi.mealrecipe.data

import com.shivangi.mealrecipe.model.FavoriteMeal
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoriteMealDao: FavoriteMealDao) {

    val allFavorites: Flow<List<FavoriteMeal>> = favoriteMealDao.getAllFavorites()

    suspend fun insertFavorite(meal: FavoriteMeal) {
        favoriteMealDao.insertFavorite(meal)
    }

    suspend fun deleteFavorite(meal: FavoriteMeal) {
        favoriteMealDao.deleteFavorite(meal)
    }

    suspend fun isFavorite(mealId: String): Boolean {
        return favoriteMealDao.isFavorite(mealId)
    }
}
