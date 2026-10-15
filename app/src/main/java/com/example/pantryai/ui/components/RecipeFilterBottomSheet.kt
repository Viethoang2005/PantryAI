
package com.example.pantryai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFilterBottomSheet(
    categories: List<String>,
    difficulties: List<String>,
    selectedCategory: String?,
    selectedDifficulty: String?,
    maxCookingTimeMinutes: Int?,
    onDismiss: () -> Unit,
    onApply: (
        category: String?,
        difficulty: String?,
        maxTime: Int?
    ) -> Unit,
    onClear: () -> Unit
) {
    var draftCategory by remember(selectedCategory) {
        mutableStateOf(selectedCategory)
    }
    var draftDifficulty by remember(selectedDifficulty) {
        mutableStateOf(selectedDifficulty)
    }
    var draftMaxTime by remember(maxCookingTimeMinutes) {
        mutableStateOf(maxCookingTimeMinutes)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bộ lọc công thức",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tùy chỉnh món ăn theo sở thích của bạn.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FilterSelectField(
                label = "Danh mục",
                value = draftCategory ?: "Tất cả danh mục",
                options = categories,
                onSelected = { draftCategory = it }
            )

            FilterSelectField(
                label = "Độ khó",
                value = draftDifficulty ?: "Tất cả độ khó",
                options = difficulties,
                onSelected = { draftDifficulty = it }
            )

            FilterSelectField(
                label = "Thời gian nấu tối đa",
                value = draftMaxTime?.let { "$it phút" }
                    ?: "Không giới hạn",
                options = listOf(
                    "15 phút",
                    "30 phút",
                    "45 phút",
                    "60 phút"
                ),
                onSelected = { value ->
                    draftMaxTime = value
                        ?.substringBefore(" ")
                        ?.toIntOrNull()
                },
                allLabel = "Không giới hạn"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onClear()
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Đặt lại")
                }

                Button(
                    onClick = {
                        onApply(
                            draftCategory,
                            draftDifficulty,
                            draftMaxTime
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Áp dụng")
                }
            }
        }
    }
}

@Composable
private fun FilterSelectField(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String?) -> Unit,
    allLabel: String = "Tất cả"
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(value)
                Text("⌄")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(allLabel) },
                onClick = {
                    onSelected(null)
                    expanded = false
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
