package com.example.pantryai.data.repository

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRepository @Inject constructor(){
    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.5-flash-lite")

    suspend fun ask(prompt: String) : String {
        val response = model.generateContent(prompt)
        return response.text ?: "AI chưa trả về nội dung"
    }
}