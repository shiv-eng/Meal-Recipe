// File: com/shivangi.mealrecipe/viewModels/SearchMealViewModel.kt

package com.shivangi.mealrecipe.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.shivangi.mealrecipe.model.Meal

data class SearchMealState(
    val loading: Boolean = false,
    val meals: List<Meal>? = null,
    val error: String? = null
)

class SearchMealViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchMealState = MutableStateFlow(SearchMealState())
    val searchMealState: StateFlow<SearchMealState> = _searchMealState.asStateFlow()

    // Provide a public function to set the query
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun fetchSearchMeal() {
        viewModelScope.launch {
            _searchMealState.value = SearchMealState(loading = true)
            try {
                // Replace with actual API call or logic
                val meals = listOf<Meal>() // For demo, just an empty list
                _searchMealState.value = SearchMealState(meals = meals)
            } catch (e: Exception) {
                _searchMealState.value = SearchMealState(error = e.message)
            }
        }
    }
}
