

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
    val youtubeLink: String?
)
