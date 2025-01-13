package com.shivangi.mealrecipe.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivangi.mealrecipe.apiService.mealService
import com.shivangi.mealrecipe.model.SearchMealState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchMealViewModel : ViewModel() {

    val searchQuery = mutableStateOf("")
    private val _searchMealState = MutableStateFlow(SearchMealState())
    val searchMealState: StateFlow<SearchMealState> = _searchMealState

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun fetchSearchMeal() {
        viewModelScope.launch {
            try {
                if (searchQuery.value.isEmpty()) {
                    _searchMealState.value = SearchMealState(
                        loading = false,
                        error = "Meal name can't be empty"
                    )
                    return@launch
                }

                _searchMealState.value = SearchMealState(loading = true)
                val searchResponse = mealService.searchMeal(searchQuery.value)
                _searchMealState.value = SearchMealState(
                    loading = false,
                    meals = searchResponse.meals
                )
            } catch (e: Exception) {
                _searchMealState.value = SearchMealState(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
