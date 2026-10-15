package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.data.repository.ChatHistoryRepository
import com.example.pantryai.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val chatHistoryRepository: ChatHistoryRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking = _isThinking.asStateFlow()

    init {
        viewModelScope.launch {
            chatHistoryRepository.ensureWelcomeMessage()

            chatHistoryRepository.getMessages().collect { savedMessages ->
                _messages.value = savedMessages
            }
        }
    }
    fun sendMessage(input: String) {
        val text = input.trim()

        if (text.isBlank() || _isThinking.value) return

        viewModelScope.launch {
            _isThinking.value = true

            try {
                chatHistoryRepository.saveMessage(
                    content = text,
                    isFromUser = true
                )

                delay(500.milliseconds)

                val (reply, recipes) = generateMockReply(text)

                chatHistoryRepository.saveMessage(
                    content = reply,
                    isFromUser = false,
                    suggestedRecipes = recipes
                )
            } finally {
                _isThinking.value = false
            }
        }
    }

    private fun generateMockReply(input: String): Pair<String, List<Recipe>> {
        val stopWords = setOf(
            "toi", "tôi", "co", "có", "va", "và",
            "voi", "với", "muon", "muốn", "nau", "nấu",
            "mon", "món", "gi", "gì", "lam", "làm",
            "cho", "minh", "mình", "cac", "các",
            "nhung", "những", "nguyen", "liệu"
        )

        val keywords = Regex("[\\p{L}\\p{N}]+")
            .findAll(input.lowercase())
            .map { it.value }
            .filter { it.length >= 3 && it !in stopWords }
            .toSet()


        if (keywords.isEmpty()) {
            return "Bạn hãy nhập tên nguyên liệu cụ thể nhé! 🥗" to emptyList()
        }

        val matchedRecipes = recipeRepository.getRecipes()
            .map { recipe ->
                val searchableText = (
                        listOf(recipe.name) + recipe.ingredients
                        ).joinToString(" ").lowercase()

                val score = keywords.count { keyword ->
                    keyword in searchableText
                }

                recipe to score
            }
            .filter { (_, score) -> score > 0 }
            .sortedByDescending { (_, score) -> score }
            .take(3)
            .map { (recipe, _) -> recipe }

        if (matchedRecipes.isEmpty()) {
            return "Mình chưa tìm thấy công thức phù hợp trong danh sách hiện tại. Bạn thử nhập nguyên liệu khác nhé! 🥗" to
                    emptyList()
        }

        return "Mình tìm được ${matchedRecipes.size} công thức có thể phù hợp với nguyên liệu của bạn. Nhấn vào món ăn để xem nguyên liệu và cách làm nhé! 🍳" to
                matchedRecipes
    }

    fun clearChatHistory() {
        if(_isThinking.value) return

        viewModelScope.launch {
            chatHistoryRepository.clearHistory()
        }
    }
}