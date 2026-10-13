package com.example.pantryai.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pantryai.data.mock.FakeRecipes
import com.example.pantryai.ui.components.PantryBottomAppBar
import com.example.pantryai.ui.screens.AssistantScreen
import com.example.pantryai.ui.screens.FavoritesScreen
import com.example.pantryai.ui.screens.HomeScreen
import com.example.pantryai.ui.screens.RecipesScreen
import com.example.pantryai.ui.screens.SplashScreen

@Composable
fun PantryNavBar(){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.HOME,
        Screen.RECIPES,
        Screen.ASSISTANT,
        Screen.FAVORITES
    )

    Scaffold(
        bottomBar = {
            if(showBottomBar) {
                PantryBottomAppBar(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.SPLASH,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            composable(Screen.SPLASH) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.HOME) {
                            popUpTo(Screen.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.HOME) {
                var query by remember {
                    mutableStateOf("")
                }

                val filteredRecipes =
                    FakeRecipes.recipes.filter { recipe ->
                        recipe.name.contains(
                            query,
                            ignoreCase = true
                        ) || recipe.ingredients.any { ingredient ->
                            ingredient.contains(
                                query,
                                ignoreCase = true
                            )
                        }
                    }

                HomeScreen(
                    query = query,
                    onQueryChange = { query = it },
                    recipes = filteredRecipes,
                    onAskAi = {
                    },
                    onRecipeClick = {

                    }
                )
            }

            composable(Screen.RECIPES) {
                RecipesScreen()
            }

            composable(Screen.ASSISTANT) {
                AssistantScreen()
            }

            composable(Screen.FAVORITES) {
                FavoritesScreen()
            }
        }
    }
}