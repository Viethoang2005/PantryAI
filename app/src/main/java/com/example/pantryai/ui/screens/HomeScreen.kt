package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryGreenLight
import com.example.pantryai.ui.theme.PantryText
import com.example.pantryai.ui.theme.PantryTextSecondary

@Composable
fun HomeScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    recipes: List<Recipe>,
    onAskAi: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
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

        item {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Tìm món ăn hoặc nguyên liệu...")
                }
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PantryGreenLight
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "✨ Trợ lý nấu ăn AI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PantryGreen
                    )

                    Text(
                        text = "Bạn có nguyên liệu gì? " +
                                "Hãy để PantryAI gợi ý món ăn.",
                        color = PantryText
                    )

                    Button(
                        onClick = onAskAi,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PantryGreen
                        )
                    ) {
                        Text("Hỏi AI ngay")
                    }
                }
            }
        }

        item {
            Text(
                text = "Món ăn gợi ý",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PantryText
            )
        }

        items(recipes, key = { it.id }) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                onClick = {
                    onRecipeClick(recipe)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = recipe.emoji,
                        fontSize = 36.sp
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = recipe.name,
                            fontWeight = FontWeight.Bold,
                            color = PantryText
                        )

                        Text(
                            text = recipe.description,
                            color = PantryTextSecondary,
                            fontSize = 13.sp
                        )

                        Text(
                            text = "${recipe.cookingTimeMinutes} phút • " +
                                    recipe.difficulty,
                            color = PantryGreen,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        query = "",
        onQueryChange = {_ -> },
        recipes = emptyList(),
        onAskAi = {},
        onRecipeClick = {}
    )
}
