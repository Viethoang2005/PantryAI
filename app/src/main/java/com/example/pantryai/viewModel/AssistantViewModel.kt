package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.repository.AssistantRepository
import com.example.pantryai.data.repository.ChatHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val assistantRepository: AssistantRepository,
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

                val (reply, recipes) = assistantRepository.generateReply(text)

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

    fun clearChatHistory() {
        if(_isThinking.value) return

        viewModelScope.launch {
            chatHistoryRepository.clearHistory()
        }
    }
}