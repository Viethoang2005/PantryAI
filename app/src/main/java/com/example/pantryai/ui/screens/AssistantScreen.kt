package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.ui.components.ChatInputBar
import com.example.pantryai.ui.components.ChatMessageBubble
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryText
import com.example.pantryai.ui.theme.PantryTextSecondary
import com.example.pantryai.viewModel.AssistantViewModel

@Composable
fun AssistantScreen(
    onRecipeClick: (Recipe) -> Unit,
    viewModel: AssistantViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isThinking by viewModel.isThinking.collectAsStateWithLifecycle()

    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(messages.size, isThinking) {
        val lastMessageIndex = messages.lastIndex
        val targetIndex = if (isThinking) {
            lastMessageIndex + 1
        } else {
            lastMessageIndex
        }

        if (targetIndex >= 0) {
            listState.animateScrollToItem(targetIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Trợ lý nấu ăn AI 🥑",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PantryGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Cho mình biết nguyên liệu bạn đang có nhé!",
                style = MaterialTheme.typography.bodyMedium,
                color = PantryTextSecondary
            )
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = messages,
                key = { message -> message.id }
            ) { message ->
                ChatMessageBubble(message = message, onRecipeClick = onRecipeClick)
            }

            if (isThinking) {
                item(key = "thinking_indicator") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(18.dp),
                                    color = PantryGreen,
                                    strokeWidth = 2.dp
                                )

                                Text(
                                    text = "Đang suy nghĩ...",
                                    color = PantryText
                                )
                            }
                        }
                    }
                }
            }
        }

        ChatInputBar(
            value = input,
            onValueChange = { input = it },
            onSend = {
                val message = input.trim()

                if (message.isNotEmpty() && !isThinking) {
                    viewModel.sendMessage(message)
                    input = ""

                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            },
            isThinking = isThinking
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    AssistantScreen(
        onRecipeClick = {}
    )
}