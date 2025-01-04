package com.shivangi.mealrecipe.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivangi.mealrecipe.apiService.mealService
import com.shivangi.mealrecipe.model.MealResponse
import com.shivangi.mealrecipe.model.RandomMealState
import kotlinx.coroutines.launch

class RandomMealViewModel : ViewModel() {
    private val _randomMealState = mutableStateOf(RandomMealState())
    val randomMeal: State<RandomMealState> = _randomMealState

    init {
        fetchMeals()
    }

    private fun fetchMeals() {
        viewModelScope.launch {
            try {
                val response: MealResponse = mealService.getRandomMeal()
                _randomMealState.value = _randomMealState.value.copy(
                    loading = false,
                    meal = response.meals.firstOrNull(),
                    error = null,
                )
            } catch (e: Exception) {
                _randomMealState.value = _randomMealState.value.copy(
                    loading = false,
                    meal = null,
                    error = "Error fetching a random meal : ${e.message}",
                )
            }
        }
    }

}
