
package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.components.RecipeCard
import com.example.pantryai.ui.theme.PantryCream

@Composable
fun FavoritesScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream)
            .padding(horizontal = 20.dp)

    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Món ăn đã lưu ❤️",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Những công thức bạn muốn nấu sau này",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("♡", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Bạn chưa lưu công thức nào",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Mở một công thức và nhấn biểu tượng trái tim.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 8.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(recipes, key = { it.id }) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoriteScreenPreview() {
    FavoritesScreen(
        emptyList(),
        onRecipeClick = {}
    )
}