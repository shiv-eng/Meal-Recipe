package com.shivangi.mealrecipe.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shivangi.mealrecipe.viewModels.SearchMealViewModel

@Composable
fun SearchMealScreen(modifier: Modifier = Modifier) {
    val searchViewModel: SearchMealViewModel = viewModel()
    val viewState by searchViewModel.searchMealState
    val searchQueue = searchViewModel.searchQuery

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Box
        OutlinedTextField(
            value = searchQueue.value,
            onValueChange = { text -> searchQueue.value = text },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(2.dp, RoundedCornerShape(12.dp)),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search for your favorite dish...",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        searchViewModel.fetchSearchMeal()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        // Results
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewState.loading -> {
                    BoxText(error = "Loading...")
                }
                viewState.error != null -> {
                    BoxText(viewState.error!!)
                }
                viewState.meals.isNullOrEmpty() -> {
                    BoxText("No results. Try different keywords.")
                }
                else -> {
                    viewState.meals?.let { MealList(meals = it) }
                }
            }
        }
    }
}
