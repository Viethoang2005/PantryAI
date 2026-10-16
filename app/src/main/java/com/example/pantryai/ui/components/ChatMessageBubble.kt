
package com.example.pantryai.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryText

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (message.isFromUser) 18.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 18.dp
            ),
            color = if (message.isFromUser) {
                PantryGreen
            } else {
                MaterialTheme.colorScheme.surface
            },
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (message.isFromUser) {
                        Color.White
                    } else {
                        PantryText
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                message.suggestedRecipes.forEach { recipe ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRecipeClick(recipe)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row {
                                Text(
                                    text = recipe.emoji,
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = recipe.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = PantryGreen
                                    )

                                    Text(
                                        text = recipe.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = "⏱ ${recipe.cookingTimeMinutes} phút  •  ${recipe.difficulty}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "Nhấn để xem chi tiết →",
                                style = MaterialTheme.typography.labelMedium,
                                color = PantryGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatMessageBubblePreview() {
    ChatMessageBubble(
        ChatMessage(1L,"", true,emptyList()),
        {}
    )
}

