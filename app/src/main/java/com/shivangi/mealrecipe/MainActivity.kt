// ----------------------
// MainActivity.kt
// Single top bar across the entire app.
// The title becomes "Favorite Meals" if current route is FavoriteScreen, else "Meal Recipe".
// Search is unchanged, uses the original logic from your SearchMealViewModel.
// ----------------------
package com.shivangi.mealrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.shivangi.mealrecipe.ui.theme.RentalsTheme
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel
import com.shivangi.mealrecipe.viewModels.SearchMealViewModel
import com.shivangi.mealrecipe.views.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentalsTheme {
                val navController = rememberNavController()

                val searchVM: SearchMealViewModel = viewModel()
                val favoritesVM: FavoritesViewModel = viewModel()

                val searchState by searchVM.searchMealState.collectAsState()

                var isSearching by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf(TextFieldValue("")) }

                // Observe current route to switch top bar title if on favorites
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                val topBarTitle = if (currentRoute == Screen.FavoriteScreen.route) "Favorite Meals"
                else "Meal Recipe"

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                if (isSearching) {
                                    OutlinedTextField(
                                        value = searchText,
                                        onValueChange = { searchText = it },
                                        singleLine = true,
                                        placeholder = { Text("Type a dish...") },
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                searchVM.setSearchQuery(searchText.text)
                                                searchVM.fetchSearchMeal()
                                                isSearching = false
                                            }
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Text(topBarTitle)
                                }
                            },
                            navigationIcon = {
                                if (isSearching) {
                                    IconButton(onClick = {
                                        isSearching = false
                                        searchText = TextFieldValue("")
                                        // Re-fetch or clear results if you want
                                        searchVM.fetchSearchMeal()
                                    }) {
                                        Icon(Icons.Filled.Close, contentDescription = null)
                                    }
                                }
                            },
                            actions = {
                                if (!isSearching) {
                                    IconButton(onClick = { isSearching = true }) {
                                        Icon(Icons.Filled.Search, contentDescription = null)
                                    }
                                } else {
                                    IconButton(onClick = {
                                        searchVM.setSearchQuery(searchText.text)
                                        searchVM.fetchSearchMeal()
                                        isSearching = false
                                    }) {
                                        Icon(Icons.Filled.Check, contentDescription = null)
                                    }
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.RandomMealScreen.route)
                                }) {
                                    Icon(Icons.Filled.Shuffle, contentDescription = null)
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.FavoriteScreen.route) {
                                        popUpTo(Screen.RandomMealScreen.route) { inclusive = false }
                                    }
                                }) {
                                    Icon(Icons.Filled.Favorite, contentDescription = null)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        // If user is searching and typed something, show those results
                        if (isSearching && searchText.text.isNotBlank()) {
                            when {
                                searchState.loading -> {
                                    BoxText("Searching...")
                                }
                                searchState.error != null -> {
                                    BoxText("Error: ${searchState.error}")
                                }
                                !searchState.meals.isNullOrEmpty() -> {
                                    // Show the meal list with favorites
                                    MealList(
                                        meals = searchState.meals!!,
                                        favoritesViewModel = favoritesVM
                                    )
                                }
                                else -> {
                                    BoxText("No results found.")
                                }
                            }
                        } else {
                            // Normal navigation
                            Navigation(navController, innerPadding)
                        }
                    }
                }
            }
        }
    }
}
