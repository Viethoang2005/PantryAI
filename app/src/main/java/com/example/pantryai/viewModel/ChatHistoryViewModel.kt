package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.local.entity.ChatConversationEntity
import com.example.pantryai.data.repository.ChatConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatHistoryViewModel @Inject constructor(
    private val repository: ChatConversationRepository
) : ViewModel() {
    val conversations = repository.getAllConversations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun createConversation(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.createConversation()
            onCreated(id)
        }
    }

    fun deleteConversation(conversation: ChatConversationEntity) {
        viewModelScope.launch {
            repository.deleteConversation(conversation.id)
        }
    }

    fun renameConversation(conversationId: Long, newTitle: String) {
        viewModelScope.launch {
            repository.renameConversation(conversationId, newTitle)
        }
    }
}