package com.example.pantryai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.pantryai.ui.components.ConfirmDeleteDialog
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryText
import com.example.pantryai.ui.theme.PantryTextSecondary
import com.example.pantryai.viewModel.AssistantViewModel
import kotlinx.coroutines.launch

@Composable
fun AssistantScreen(
    onRecipeClick: (Recipe) -> Unit,
    viewModel: AssistantViewModel = hiltViewModel(),
    selectedConversationId: Long? = null,
    onSelectedConversationHandle: () -> Unit = {}
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isThinking by viewModel.isThinking.collectAsStateWithLifecycle()

    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val currentConversationId by viewModel.currentConversationId.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var showClearDialog by remember { mutableStateOf(false) }
    var conversationToDelete by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(selectedConversationId) {
        selectedConversationId?.let { id ->
            viewModel.selectConversation(id)
            onSelectedConversationHandle()
        }
    }

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


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "PantryAI",
                        style = MaterialTheme.typography.headlineSmall,
                        color = PantryGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.createNewConversation()
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isThinking
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Text(
                            text = "  Chat mới",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "LỊCH SỬ TRÒ CHUYỆN",
                        style = MaterialTheme.typography.labelMedium,
                        color = PantryTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(
                            items = conversations,
                            key = { it.id }
                        ) { conversation ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = if(conversation.id == currentConversationId)
                                            PantryGreen.copy(alpha = 0.12f)
                                        else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        viewModel.selectConversation(conversation.id)

                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = PantryGreen
                                )

                                Text(
                                    text = conversation.title,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 10.dp),
                                    maxLines = 2,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                IconButton(
                                    onClick = {
                                        conversationToDelete = conversation.id
                                    },
                                    enabled = !isThinking
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xóa cuộc trò chuyện",
                                        tint = Color(0xFFB42318)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Mở lịch sử trò chuyện"
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
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
                IconButton(
                    onClick = {
                        showClearDialog = true
                    },
                    enabled = !isThinking
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa lịch sử trò chuyện",
                        tint = Color(0xFFB42318)
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.createNewConversation()
                    },
                    enabled = !isThinking
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tạo cuộc trò chuyện mới"
                    )
                }


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
    if(showClearDialog) {
        ConfirmDeleteDialog(
            title = "Xóa lịch sử trò chuyện?",
            message = "Toàn bộ tin nhắn hiện tại sẽ bị xóa. Bạn có chắc chắn muốn tiếp tục không?",
            onConfirm = {
                viewModel.clearChatHistory()
                showClearDialog = false
            },
            onDismiss = {
                showClearDialog = false
            }
        )
    }
    if (conversationToDelete != null) {
        ConfirmDeleteDialog(
            title = "Xóa cuộc trò chuyện?",
            message = "Toàn bộ tin nhắn trong cuộc trò chuyện này sẽ bị xóa vĩnh viễn. Bạn có chắc chắn không?",
            onConfirm = {
                conversationToDelete?.let { id ->
                    viewModel.deleteConversation(id)
                }
                conversationToDelete = null
            },
            onDismiss = {
                conversationToDelete = null
            }
        )
    }


}


@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    AssistantScreen(
        onRecipeClick = {},
        selectedConversationId = 0L,
        onSelectedConversationHandle = {}
    )
}