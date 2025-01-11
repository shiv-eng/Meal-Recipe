// File: com/shivangi/mealrecipe/model/FavoriteMeal.kt

package com.shivangi.mealrecipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey val mealId: String,
    val name: String,
    val thumbnail: String,
    val origin: String?,
    val instructions: String,
    val ingredient: String,
    val youtubeLink: String?
) {
    fun getIngredients(): List<IngredientWithMeasurement> {
        if (ingredient.isBlank()) return emptyList()
        val parts = ingredient.split(":").map { it.trim() }
        return if (parts.size >= 2) {
            listOf(IngredientWithMeasurement(parts[0], parts[1]))
        } else {
            listOf(IngredientWithMeasurement(parts[0], ""))
        }
    }
}
