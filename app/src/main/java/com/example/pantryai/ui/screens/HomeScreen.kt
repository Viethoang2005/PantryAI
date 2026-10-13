package com.example.pantryai.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pantryai.data.model.Recipe

@Composable
fun HomeScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    recipes: List<Recipe>,
    onAskAi: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen",
            fontSize = 35.sp
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        "",
        onQueryChange = {_ -> },
        recipes = emptyList(),
        onAskAi = {},
        onRecipeClick = {_ -> }
    )
}