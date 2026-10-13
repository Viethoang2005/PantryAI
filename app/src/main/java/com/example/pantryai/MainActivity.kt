package com.example.pantryai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pantryai.data.mock.FakeRecipes
import com.example.pantryai.ui.screens.HomeScreen
import com.example.pantryai.ui.screens.SplashScreen
import com.example.pantryai.ui.theme.PantryAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryAITheme {
                var showSplash by remember { mutableStateOf(true) }
                if(showSplash) {
                    SplashScreen(
                        onSplashFinished = {
                            showSplash = false
                        }
                    )
                }else {
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

            }
        }
    }
}
