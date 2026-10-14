
package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.components.RecipeCard
import com.example.pantryai.ui.components.RecipeFilterBottomSheet
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen

@Composable
fun RecipesScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    categories: List<String>,
    difficulties: List<String>,
    selectedCategory: String?,
    selectedDifficulty: String?,
    maxCookingTimeMinutes: Int?,
    onCategoryChange: (String?) -> Unit,
    onDifficultyChange: (String?) -> Unit,
    onMaxCookingTimeChange: (Int?) -> Unit,
    onClearFilters: () -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Khám phá món ngon 🍳",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Tìm món ăn phù hợp với bạn",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tên món ăn hoặc nguyên liệu...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { showFilterSheet = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Lọc công thức",
                        tint = PantryGreen
                    )
                }
            }
        )
        Spacer(Modifier.height(16.dp))

        if (showFilterSheet) {
            RecipeFilterBottomSheet(
                categories = categories,
                difficulties = difficulties,
                selectedCategory = selectedCategory,
                selectedDifficulty = selectedDifficulty,
                maxCookingTimeMinutes = maxCookingTimeMinutes,
                onDismiss = {
                    showFilterSheet = false
                },
                onApply = { category, difficulty, maxTime ->
                    onCategoryChange(category)
                    onDifficultyChange(difficulty)
                    onMaxCookingTimeChange(maxTime)
                },
                onClear = onClearFilters
            )
        }

        Text(
            text = "${recipes.size} công thức",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Không tìm thấy công thức phù hợp.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.weight(1f)
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

@Preview
@Composable
fun RecipesScreenPreview() {
    RecipesScreen(
        emptyList(),
        {},
    "",
        { _ -> },
    emptyList(),
    emptyList(),
    null,
    null,
    20,
        { _ -> },
        { _ -> },
        { _ -> },
        {}
    )
}
