package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GeminiTestViewModel @Inject constructor(
    private val repository: GeminiRepository
) : ViewModel() {

    private val _result = MutableStateFlow("Chưa gửi yêu cầu.")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun testGemini() {
        if (_loading.value) return

        viewModelScope.launch {
            _loading.value = true
            _result.value = "Đang kết nối Gemini..."

            try {
                _result.value = repository.ask(
                    "Trả lời bằng tiếng Việt: Hãy chào tôi và giới thiệu ngắn gọn về PantryAI."
                )
            } catch (e: Exception) {
                _result.value =
                    "Lỗi: ${e.javaClass.simpleName}\n${e.message ?: "Không có chi tiết lỗi"}"
            } finally {
                _loading.value = false
            }
        }
    }
}
