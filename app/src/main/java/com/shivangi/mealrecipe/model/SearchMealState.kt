package com.shivangi.mealrecipe.model

data class SearchMealState(
    val loading: Boolean = false,
    val meals: List<Meal>? = null,
    val error: String? = null
)
