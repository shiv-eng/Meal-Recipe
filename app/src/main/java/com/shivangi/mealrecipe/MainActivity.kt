package com.shivangi.mealrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.shivangi.mealrecipe.ui.theme.RentalsTheme
import com.shivangi.mealrecipe.viewModels.FavoritesViewModel
import com.shivangi.mealrecipe.viewModels.SearchMealViewModel
import com.shivangi.mealrecipe.views.*

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

                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route
                val topTitle = when (currentRoute) {
                    Screen.FavoriteScreen.route -> "Favorite Meals"
                    else -> "Meal Recipe"
                }

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
                                    Text(topTitle)
                                }
                            },
                            navigationIcon = {
                                if (isSearching) {
                                    IconButton(onClick = {
                                        isSearching = false
                                        searchText = TextFieldValue("")
                                        searchVM.setSearchQuery("")
                                        searchVM.fetchSearchMeal()
                                    }) {
                                        Icon(Icons.Filled.Close, null)
                                    }
                                }
                            },
                            actions = {
                                if (!isSearching) {
                                    IconButton(onClick = { isSearching = true }) {
                                        Icon(Icons.Filled.Search, null)
                                    }
                                } else {
                                    IconButton(onClick = {
                                        searchVM.setSearchQuery(searchText.text)
                                        searchVM.fetchSearchMeal()
                                        isSearching = false
                                    }) {
                                        Icon(Icons.Filled.Check, null)
                                    }
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.RandomMealScreen.route)
                                }) {
                                    Icon(Icons.Filled.Shuffle, null)
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.FavoriteScreen.route) {
                                        popUpTo(Screen.RandomMealScreen.route) { inclusive = false }
                                    }
                                }) {
                                    Icon(Icons.Filled.Favorite, null)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(0.dp)
                    ) {
                        if (isSearching && searchText.text.isNotBlank()) {
                            when {
                                searchState.loading -> BoxText("Searching...")
                                searchState.error != null -> BoxText("Error: ${searchState.error}")
                                !searchState.meals.isNullOrEmpty() -> {
                                    MealList(
                                        meals = searchState.meals!!,
                                        favoritesViewModel = favoritesVM
                                    )
                                }
                                else -> BoxText("No results found.")
                            }
                        } else {
                            Navigation(navController, innerPadding)
                        }
                    }
                }
            }
        }
    }
}
