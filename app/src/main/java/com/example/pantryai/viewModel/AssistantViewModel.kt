package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.local.entity.ChatConversationEntity
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.repository.AssistantRepository
import com.example.pantryai.data.repository.ChatConversationRepository
import com.example.pantryai.data.repository.ChatHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val assistantRepository: AssistantRepository,
    private val chatHistoryRepository: ChatHistoryRepository,
    private val conversationRepository: ChatConversationRepository
) : ViewModel() {
    val conversations: StateFlow<List<ChatConversationEntity>> =
        conversationRepository.getAllConversations().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()
    private val _currentConversationId = MutableStateFlow<Long?>(null)
    val currentConversationId = _currentConversationId.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking = _isThinking.asStateFlow()

    private val _isInitializing = MutableStateFlow(true)
    val isInitializing = _isInitializing.asStateFlow()

    private var messagesJob: Job? = null
    init {
        initializeConversation()
    }
    private fun initializeConversation() {
        viewModelScope.launch {
            try {
                val conversations = conversationRepository.getAllConversations().first()

                val conversationId = if(conversations.isEmpty()) conversationRepository.createConversation()
                else conversations.first().id
                selectConversation(conversationId)
            }finally {
                _isInitializing.value = false
            }
        }
    }

    fun selectConversation(conversationId: Long) {
        if(_currentConversationId.value == conversationId) return
        _currentConversationId.value = conversationId
        _messages.value = emptyList()
        messagesJob?.cancel()

        messagesJob = viewModelScope.launch {
            chatHistoryRepository.ensureWelcomeMessage(conversationId)

            chatHistoryRepository.getMessages(conversationId)
                .collect { savedMessages -> _messages.value = savedMessages }
        }
    }

    fun createNewConversation() {
        if(_isThinking.value) return

        viewModelScope.launch {
            val newId = conversationRepository.createConversation()
            selectConversation(newId)
        }
    }
    fun sendMessage(input: String) {
        val text = input.trim()
        val conversationId = _currentConversationId.value


        if (text.isBlank() || _isThinking.value || conversationId == null) return

        viewModelScope.launch {
            _isThinking.value = true

            try {
                chatHistoryRepository.saveMessage(
                    conversationId = conversationId,
                    content = text,
                    isFromUser = true
                )
                updateConversationTitleIfNeeded(
                    conversationId = conversationId,
                    message = text
                )

                val result = assistantRepository.generateReply(text)

                chatHistoryRepository.saveMessage(
                    conversationId = conversationId,
                    content = result.message,
                    isFromUser = false,
                    suggestedRecipes = result.recipes
                )
            } catch (e: Exception) {
                chatHistoryRepository.saveMessage(
                    conversationId = conversationId,
                    content = "Xin lỗi, mình gặp lỗi khi xử lý yêu cầu. Bạn hãy thử lại nhé.",
                    isFromUser = false
                )
            }
            finally {
                _isThinking.value = false
            }
        }
    }

    fun clearChatHistory() {
        val conversationId = _currentConversationId.value ?: return
        if(_isThinking.value) return

        viewModelScope.launch {
            chatHistoryRepository.clearHistory(conversationId = conversationId)
        }
    }

    fun deleteConversation(conversationId: Long) {
        if(_isThinking.value) return

        viewModelScope.launch {
            conversationRepository.deleteConversation(conversationId)

            if(_currentConversationId.value == conversationId) {
                val remaining = conversationRepository.getAllConversations().first()

                if(remaining.isNotEmpty()) selectConversation(remaining.first().id)
                else {
                    val newId = conversationRepository.createConversation()
                    selectConversation(newId)
                }
            }
        }
    }

    private suspend fun updateConversationTitleIfNeeded(
        conversationId: Long,
        message: String
    ) {
        val conversation = conversations.value
            .find { it.id == conversationId }
            ?: return

        if (conversation.title != "Cuộc trò chuyện mới") return

        val title = message
            .trim()
            .replace(Regex("\\s+"), " ")
            .take(40)
            .let {
                if (message.trim().length > 40) "$it..." else it
            }

        conversationRepository.renameConversation(
            conversationId = conversationId,
            newTitle = title
        )
    }
}