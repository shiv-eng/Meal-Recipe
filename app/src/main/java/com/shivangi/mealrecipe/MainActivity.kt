package com.shivangi.mealrecipe


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.navigation.compose.rememberNavController
import com.shivangi.mealrecipe.ui.theme.RentalsTheme
import com.shivangi.mealrecipe.views.Navigation
import com.shivangi.mealrecipe.views.Screen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            RentalsTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Meal Recipe") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            actions = {
                                // 1. Search Icon
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.SearchMealScreen.route) {
                                            popUpTo(Screen.RandomMealScreen.route) {
                                                inclusive = false
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                // 2. Random (Shuffle) Icon
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.RandomMealScreen.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Shuffle,
                                        contentDescription = "Random",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                // 3. Favorite Icon
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.FavoriteScreen.route) {
                                            popUpTo(Screen.RandomMealScreen.route) {
                                                inclusive = false
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Favorite",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // The rest of your NavHost
                    Navigation(navController, innerPadding)
                }
            }
        }
    }
}
