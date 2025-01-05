package com.shivangi.mealrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.compose.rememberNavController
import com.shivangi.mealrecipe.ui.theme.RentalsTheme
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
                val searchMealState by searchMealViewModel.searchMealState

                var isSearching by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf(TextFieldValue("")) }

                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                var showBottomSheet by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()

                // Show bottom sheet if no results
                LaunchedEffect(searchMealState.meals, searchMealState.loading) {
                    if (!searchMealState.loading &&
                        searchMealState.meals.isNullOrEmpty() &&
                        searchText.text.isNotBlank()
                    ) {
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
                                                searchMealViewModel.searchQuery.value = searchText.text
                                                searchMealViewModel.fetchSearchMeal()
                                            }
                                        )
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
                                        searchMealViewModel.fetchSearchMeal()
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                    }
                                }
                            },
                            actions = {
                                if (!isSearching) {
                                    IconButton(onClick = { isSearching = true }) {
                                        Icon(Icons.Default.Search, contentDescription = null)
                                    }
                                } else {
                                    IconButton(onClick = {
                                        searchMealViewModel.searchQuery.value = searchText.text
                                        searchMealViewModel.fetchSearchMeal()
                                    }) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.RandomMealScreen.route)
                                }) {
                                    Icon(Icons.Default.Shuffle, contentDescription = null)
                                }
                                IconButton(onClick = {
                                    navController.navigate(Screen.FavoriteScreen.route) {
                                        popUpTo(Screen.RandomMealScreen.route) { inclusive = false }
                                    }
                                }) {
                                    Icon(Icons.Default.Favorite, contentDescription = null)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                        )
                    }
                ) { paddingValues ->
                    // Show search results if searching + text typed
                    if (isSearching && searchText.text.isNotBlank()) {
                        when {
                            searchMealState.loading -> {
                                BoxText("Searching...")
                            }
                            searchMealState.error != null -> {
                                BoxText("Error: ${searchMealState.error}")
                            }
                            !searchMealState.meals.isNullOrEmpty() -> {
                                MealList(meals = searchMealState.meals!!)
                            }
                            else -> {
                                Spacer(Modifier.padding(paddingValues))
                            }
                        }
                    } else {
                        // Normal nav content
                        Navigation(navController, paddingValues)
                    }

                    // Bottom sheet (no results)
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "No results found.\nTry a different search?",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Divider()
                                Button(onClick = { showBottomSheet = false }) {
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
