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

        val pairs = ingredient.split(";")
        return pairs.mapNotNull { part ->
            val item = part.trim()
            if (item.isNotEmpty()) {
                val sub = item.split(":")
                if (sub.size >= 2) {
                    IngredientWithMeasurement(sub[0].trim(), sub[1].trim())
                } else {
                    IngredientWithMeasurement(sub[0].trim(), "")
                }
            } else null
        }
    }
}
