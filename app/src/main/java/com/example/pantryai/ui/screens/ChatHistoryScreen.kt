
package com.example.pantryai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryai.data.local.entity.ChatConversationEntity
import com.example.pantryai.viewModel.ChatHistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryScreen(
    onBack: () -> Unit,
    onConversationClick: (Long) -> Unit,
    viewModel: ChatHistoryViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations
        .collectAsStateWithLifecycle()

    var conversationToDelete by remember {
        mutableStateOf<ChatConversationEntity?>(null)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại"
                    )
                }

                Text(
                    text = "Lịch sử trò chuyện",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        viewModel.createConversation { id ->
                            onConversationClick(id)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tạo cuộc trò chuyện mới"
                    )
                }
            }
        }
    ) { padding ->
        if (conversations.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = null
                )

                Text(
                    text = "Chưa có cuộc trò chuyện nào",
                    modifier = Modifier.padding(top = 12.dp)
                )

                Button(
                    onClick = {
                        viewModel.createConversation { id ->
                            onConversationClick(id)
                        }
                    },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Bắt đầu trò chuyện")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = conversations,
                    key = { it.id }
                ) { conversation ->
                    ConversationRow(
                        conversation = conversation,
                        onClick = {
                            onConversationClick(conversation.id)
                        },
                        onDelete = {
                            conversationToDelete = conversation
                        }
                    )
                }
            }
        }
    }

    conversationToDelete?.let { conversation ->
        AlertDialog(
            onDismissRequest = {
                conversationToDelete = null
            },
            title = {
                Text("Xóa cuộc trò chuyện?")
            },
            text = {
                Text(
                    "Bạn có chắc muốn xóa \"${conversation.title}\" " +
                            "và toàn bộ tin nhắn trong cuộc trò chuyện này không?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteConversation(conversation)
                        conversationToDelete = null
                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        conversationToDelete = null
                    }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
private fun ConversationRow(
    conversation: ChatConversationEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateText = remember(conversation.updatedAt) {
        SimpleDateFormat(
            "HH:mm - dd/MM/yyyy",
            Locale("vi", "VN")
        ).format(Date(conversation.updatedAt))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = conversation.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Xóa cuộc trò chuyện"
            )
        }
    }
}
