
package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryai.data.mock.FakeRecipes
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.components.RecipeCard
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryGreenLight
import com.example.pantryai.ui.theme.PantryText
import com.example.pantryai.ui.theme.PantryTextSecondary

@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    onAskAi: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Header
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "PantryAI 🥑",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PantryGreen
                )

                Text(
                    text = "Nấu ngon hơn mỗi ngày",
                    color = PantryTextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // AI assistant card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PantryGreenLight
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "✨ Trợ lý nấu ăn AI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PantryGreen
                    )

                    Text(
                        text = "Bạn đang có nguyên liệu gì? " +
                                "Hãy để PantryAI gợi ý món ngon cho bạn.",
                        color = PantryText
                    )

                    Button(
                        onClick = onAskAi,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PantryGreen
                        )
                    ) {
                        Text("Hỏi AI ngay")
                    }
                }
            }
        }

        // Suggested recipes heading
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Món ăn gợi ý",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PantryText
                )

                Text(
                    text = "Một vài món ngon dành cho bạn",
                    fontSize = 13.sp,
                    color = PantryTextSecondary
                )
            }
        }

        // Recipe list
        items(
            items = recipes,
            key = { it.id }
        ) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        recipes = FakeRecipes.recipes,
        onAskAi = {},
        onRecipeClick = {}
    )
}
