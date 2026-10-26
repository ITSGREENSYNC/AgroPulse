package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgroRepository
import com.example.data.SampleData
import com.example.model.ChatMessage
import com.example.model.CropDiseaseDetection
import com.example.model.MessageSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "msg_welcome",
            sender = MessageSender.AGRONOMIST_AI,
            text = "Welcome to AgroPulse AI! I am your precision agronomist. Point your camera at any distressed leaf to run a real-time diagnosis, ask me questions about disease control, spray windows, or calculate tank-mix dilutions for your farm."
        )
    ),
    val attachedDiagnosis: CropDiseaseDetection? = null,
    val inputText: String = "",
    val isThinking: Boolean = false,
    val quickPrompts: List<String> = SampleData.quickChatPrompts
)

class ChatViewModel(private val repository: AgroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun updateInput(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun attachDiagnosis(diagnosis: CropDiseaseDetection?) {
        _uiState.value = _uiState.value.copy(attachedDiagnosis = diagnosis)
    }

    fun sendMessage(customPrompt: String? = null, language: com.example.model.AppLanguage = com.example.model.AppLanguage.EN) {
        val promptToSend = (customPrompt ?: _uiState.value.inputText).trim()
        if (promptToSend.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = MessageSender.USER,
            text = promptToSend,
            attachedDiagnosis = _uiState.value.attachedDiagnosis
        )

        val updatedList = _uiState.value.messages + userMessage
        _uiState.value = _uiState.value.copy(
            messages = updatedList,
            inputText = "",
            isThinking = true
        )

        val attached = _uiState.value.attachedDiagnosis

        viewModelScope.launch {
            val responseResult = repository.chatWithAgronomist(updatedList, attached, language)
            val aiMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.AGRONOMIST_AI,
                text = responseResult.getOrElse { "I experienced a temporary network issue contacting agronomy services. Here is general guidance: ensure proper leaf ventilation and spray at early symptom appearance." }
            )
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + aiMessage,
                isThinking = false
            )
        }
    }
}
