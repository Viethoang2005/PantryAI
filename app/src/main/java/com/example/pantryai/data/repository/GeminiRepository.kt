package com.example.pantryai.data.repository

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRepository @Inject constructor() {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.5-flash-lite")

    suspend fun ask(prompt: String): String {
        try {
            val response = model.generateContent(prompt)

            return response.text
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalStateException(
                    "Gemini trả về nội dung rỗng."
                )

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {
            android.util.Log.e(
                "GeminiRepository",
                "Lỗi khi gọi Gemini",
                e
            )
            throw e
        }
    }
}
