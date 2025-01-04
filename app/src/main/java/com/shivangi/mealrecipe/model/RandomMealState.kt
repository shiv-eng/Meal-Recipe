package com.shivangi.mealrecipe.model

data class RandomMealState(
    val loading: Boolean = true,
    val meal: Meal? = null,
    val error: String? = null
)
