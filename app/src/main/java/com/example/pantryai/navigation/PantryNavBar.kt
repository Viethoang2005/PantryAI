package com.example.pantryai.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pantryai.data.mock.FakeRecipes
import com.example.pantryai.features.favorites.FavoriteViewModel
import com.example.pantryai.features.recipes.RecipeViewModel
import com.example.pantryai.ui.components.PantryBottomAppBar
import com.example.pantryai.ui.screens.AssistantScreen
import com.example.pantryai.ui.screens.FavoritesScreen
import com.example.pantryai.ui.screens.HomeScreen
import com.example.pantryai.ui.screens.RecipeDetailScreen
import com.example.pantryai.ui.screens.RecipesScreen
import com.example.pantryai.ui.screens.SplashScreen

@Composable
fun PantryNavBar(){
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    val recipeViewModel: RecipeViewModel = hiltViewModel()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route

    val favoriteEntities by favoriteViewModel.favorites.collectAsStateWithLifecycle()
    val favoriteIds = favoriteEntities.map { it.recipeId }.toSet()
    val favoriteRecipes = FakeRecipes.recipes.filter {
        it.id in favoriteIds
    }

    val recipes by recipeViewModel.recipes.collectAsStateWithLifecycle()
    val query by recipeViewModel.query.collectAsStateWithLifecycle()

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
                            popUpTo(navController.graph.startDestinationId)
                            { saveState = true }
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

                HomeScreen(
                    query = query,
                    onQueryChange = recipeViewModel::onQueryChange,
                    recipes = recipes,
                    onAskAi = {
                        navController.navigate(Screen.ASSISTANT)
                    },
                    onRecipeClick = { recipe ->
                        navController.navigate(Screen.recipeDetail(recipe.id))
                    }
                )
            }

            composable(Screen.RECIPES) {
                RecipesScreen(
                    recipes = recipes,
                    onRecipeClick = { recipe ->
                        navController.navigate(
                            Screen.recipeDetail(recipe.id)
                        )
                    }
                )
            }
            composable(route = Screen.RECIPE_DETAIL,
                arguments = listOf(
                    navArgument("recipeId") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->
                val recipeId = entry.arguments?.getInt("recipeId")



                if(recipeId != null) {
                    val recipe = recipeViewModel.getRecipeById(recipeId)
                    if(recipe != null) {
                        val isFavorite by favoriteViewModel.isFavorite(recipeId = recipe.id)
                            .collectAsStateWithLifecycle(initialValue = false)
                        RecipeDetailScreen(
                            recipe = recipe,
                            isFavorite = isFavorite,
                            onBack = { navController.popBackStack() },
                            onToggleFavorite = {
                                favoriteViewModel.toggleFavorite(
                                    recipeId = recipe.id,
                                    currentFavorite = isFavorite
                                )
                            }
                        )
                    }
                }
            }

            composable(Screen.ASSISTANT) {
                AssistantScreen()
            }

            composable(Screen.FAVORITES) {
                FavoritesScreen(
                    recipes = favoriteRecipes,
                    onRecipeClick = { recipe ->
                        navController.navigate(Screen.recipeDetail(recipe.id))
                    }
                )
            }
        }
    }
}
