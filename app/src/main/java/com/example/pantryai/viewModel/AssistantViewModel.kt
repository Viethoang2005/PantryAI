package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class AssistantViewModel @Inject constructor() : ViewModel() {
    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = 1L,
                content = "Xin chào! 👋 Bạn đang có những nguyên liệu nào? " +
                        "Mình sẽ gợi ý món ngon cho bạn.",
                isFromUser = false
            )
        )
    )
    val messages = _messages.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking = _isThinking.asStateFlow()

    private var nextId = 2L

    fun sendMessage(input: String) {
        val text = input.trim()

        if (text.isBlank() || _isThinking.value) return

        _messages.value += ChatMessage(
            id = nextId++,
            content = text,
            isFromUser = true
        )

        viewModelScope.launch {
            _isThinking.value = true

            try {
                delay(800.milliseconds)

                val reply = generateMockReply(text)

                _messages.value += ChatMessage(
                    id = nextId++,
                    content = reply,
                    isFromUser = false
                )
            } finally {
                _isThinking.value = false
            }
        }
    }

    private fun generateMockReply(input: String): String {
        val normalized = input.lowercase()

        return when {
            "trứng" in normalized && "cà chua" in normalized ->
                """
                🍳 Mình gợi ý món trứng xào cà chua!

                Nguyên liệu:
                • 2 quả trứng
                • 2 quả cà chua
                • Gia vị vừa đủ

                Cách làm:
                1. Rửa sạch và cắt cà chua.
                2. Đánh tan trứng rồi chiên sơ.
                3. Xào cà chua cho mềm.
                4. Cho trứng vào đảo đều và nêm gia vị.

                Thời gian: khoảng 10–15 phút.
                """.trimIndent()

            "trứng" in normalized ->
                "🥚 Bạn có thể làm trứng chiên, trứng luộc hoặc cơm chiên trứng. " +
                        "Bạn còn có thêm rau, thịt hay cơm nguội không?"

            "gà" in normalized ->
                "🍗 Bạn có thể thử gà xào sả hoặc gà áp chảo. " +
                        "Bạn có sả, tỏi hay hành không để mình gợi ý công thức phù hợp?"

            else ->
                "Mình đã nhận được nguyên liệu của bạn! 🥗 " +
                        "Bạn hãy cho mình biết thêm những nguyên liệu chính và gia vị hiện có " +
                        "để mình gợi ý món ăn phù hợp nhé."
        }
    }
}