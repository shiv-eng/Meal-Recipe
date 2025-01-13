package com.shivangi.mealrecipe.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.shivangi.mealrecipe.data.AppDatabase
import com.shivangi.mealrecipe.data.FavoritesRepository
import com.shivangi.mealrecipe.model.FavoriteMeal
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoritesRepository
    val allFavorites: LiveData<List<FavoriteMeal>>

    init {
        val dao = AppDatabase.getDatabase(application).favoriteMealDao()
        repository = FavoritesRepository(dao)
        allFavorites = repository.allFavorites.asLiveData()
    }

    fun addFavorite(meal: FavoriteMeal) = viewModelScope.launch {
        repository.insertFavorite(meal)
    }

    fun removeFavorite(meal: FavoriteMeal) = viewModelScope.launch {
        repository.deleteFavorite(meal)
    }

    suspend fun isFavorite(mealId: String): Boolean {
        return repository.isFavorite(mealId)
    }
}
