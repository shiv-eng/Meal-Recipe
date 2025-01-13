package com.shivangi.mealrecipe.data

import androidx.room.*
import com.shivangi.mealrecipe.model.FavoriteMeal
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {

    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): Flow<List<FavoriteMeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(meal: FavoriteMeal)

    @Delete
    suspend fun deleteFavorite(meal: FavoriteMeal)

    @Query("SELECT * FROM favorite_meals WHERE mealId = :mealId LIMIT 1")
    suspend fun findMealById(mealId: String): FavoriteMeal?
}
