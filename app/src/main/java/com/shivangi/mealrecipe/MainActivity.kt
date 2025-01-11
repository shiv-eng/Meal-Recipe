// Complete example in your MainActivity:

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
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
                val searchMealViewModel: SearchMealViewModel = viewModel()
                val favoritesViewModel: FavoritesViewModel = viewModel()

                val searchMealState by searchMealViewModel.searchMealState.collectAsState()

                var isSearching by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf(TextFieldValue("")) }

                // Remove the initialValue parameter if it's not recognized
                val sheetState = rememberModalBottomSheetState(
                    // No initialValue here
                    skipPartiallyExpanded = true // or skipHalfExpanded = true on older versions
                )

                val scope = rememberCoroutineScope()
                var showBottomSheet by remember { mutableStateOf(false) }

                // If search results are empty (and not loading) -> show sheet
                LaunchedEffect(searchMealState.meals, searchMealState.loading) {
                    if (!searchMealState.loading &&
                        searchMealState.meals.isNullOrEmpty() &&
                        searchText.text.isNotBlank()
                    ) {
                        scope.launch {
                            sheetState.show()
                        }
                        showBottomSheet = true
                    }
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
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Search
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                searchMealViewModel.setSearchQuery(searchText.text)
                                                searchMealViewModel.fetchSearchMeal()
                                                isSearching = false
                                            }
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Text("Meal Recipe")
                                }
                            },
                            navigationIcon = {
                                if (isSearching) {
                                    IconButton(onClick = {
                                        isSearching = false
                                        searchText = TextFieldValue("")
                                        // Optionally re-fetch if you want to clear results
                                        searchMealViewModel.fetchSearchMeal()
                                    }) {
                                        Icon(Icons.Filled.Close, contentDescription = "Close Search")
                                    }
                                }
                            },
                            actions = {
                                if (!isSearching) {
                                    IconButton(onClick = { isSearching = true }) {
                                        Icon(Icons.Filled.Search, contentDescription = "Search")
                                    }
                                } else {
                                    IconButton(onClick = {
                                        searchMealViewModel.setSearchQuery(searchText.text)
                                        searchMealViewModel.fetchSearchMeal()
                                        isSearching = false
                                    }) {
                                        Icon(Icons.Filled.Check, contentDescription = "Confirm Search")
                                    }
                                }

                                IconButton(onClick = {
                                    navController.navigate(Screen.RandomMealScreen.route)
                                }) {
                                    Icon(Icons.Filled.Shuffle, contentDescription = "Random Meal")
                                }

                                IconButton(onClick = {
                                    navController.navigate(Screen.FavoriteScreen.route) {
                                        popUpTo(Screen.RandomMealScreen.route) { inclusive = false }
                                    }
                                }) {
                                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite Meals")
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
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        // Searching UI
                        if (isSearching && searchText.text.isNotBlank()) {
                            when {
                                searchMealState.loading -> {
                                    BoxText("Searching...")
                                }
                                searchMealState.error != null -> {
                                    BoxText("Error: ${searchMealState.error}")
                                }
                                !searchMealState.meals.isNullOrEmpty() -> {
                                    MealList(
                                        meals = searchMealState.meals!!,
                                        favoritesViewModel = favoritesViewModel
                                    )
                                }
                                else -> {
                                    Spacer(Modifier.padding(paddingValues))
                                }
                            }
                        } else {
                            // Default nav content
                            Navigation(navController, paddingValues)
                        }

                        // Bottom sheet shown if no results
                        if (showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    showBottomSheet = false
                                    scope.launch { sheetState.hide() }
                                },
                                sheetState = sheetState,
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        "No results found.",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        "Try a different search?",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Button(
                                        onClick = {
                                            showBottomSheet = false
                                            scope.launch { sheetState.hide() }
                                        },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("OK")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
