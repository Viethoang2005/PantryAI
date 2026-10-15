package com.example.pantryai.ui.components

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pantryai.ui.theme.PantryGreen

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Nhập nguyên liệu hoặc câu hỏi...")
            },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4
        )

        IconButton(
            onClick = onSend,
            enabled = value.isNotBlank() && !isThinking
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Gửi tin nhắn",
                tint = if (value.isNotBlank() && !isThinking) {
                    PantryGreen
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}


@Preview
@Composable
fun ChatInputBarPreview() {
    ChatInputBar("",
        onValueChange = {_ -> },
        onSend = {},
        isThinking = true)
}