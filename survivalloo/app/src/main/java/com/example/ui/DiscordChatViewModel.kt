package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.DiscordWebhookService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class MessageStatus {
    SENDING,
    SENT,
    FAILED
}

data class DiscordChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromSelf: Boolean = true,
    val isSystemMessage: Boolean = false,
    val status: MessageStatus = MessageStatus.SENT,
    val errorMessage: String? = null
)

data class DiscordChatUiState(
    val username: String = "",
    val isUsernameConfirmed: Boolean = false,
    val messageText: String = "",
    val isSending: Boolean = false,
    val messages: List<DiscordChatMessage> = emptyList(),
    val feedbackMessage: String? = null
)

class DiscordChatViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("survivalloo_chat_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(DiscordChatUiState())
    val uiState: StateFlow<DiscordChatUiState> = _uiState.asStateFlow()

    init {
        val savedUsername = prefs.getString("saved_username", "") ?: ""
        val hasSavedUser = savedUsername.isNotBlank()

        val initialMessages = listOf(
            DiscordChatMessage(
                id = "system-welcome",
                senderName = "SurvivalLoo Bot",
                text = "👋 Vitaj v Discord chate! Zadaj svoje používateľské meno a môžeš odosielať správy priamo do nášho oficiálneho Discord kanála cez Webhook.",
                timestamp = System.currentTimeMillis(),
                isFromSelf = false,
                isSystemMessage = true,
                status = MessageStatus.SENT
            )
        )

        _uiState.value = DiscordChatUiState(
            username = savedUsername,
            isUsernameConfirmed = hasSavedUser,
            messages = initialMessages
        )
    }

    fun onUsernameChange(newName: String) {
        _uiState.update { it.copy(username = newName) }
    }

    fun confirmUsername() {
        val currentName = _uiState.value.username.trim()
        if (currentName.isNotEmpty()) {
            prefs.edit().putString("saved_username", currentName).apply()
            _uiState.update {
                it.copy(
                    username = currentName,
                    isUsernameConfirmed = true,
                    feedbackMessage = "Používateľské meno $currentName bolo nastavené!"
                )
            }
        }
    }

    fun editUsername() {
        _uiState.update { it.copy(isUsernameConfirmed = false) }
    }

    fun onMessageTextChange(newText: String) {
        _uiState.update { it.copy(messageText = newText) }
    }

    fun sendMessage(textOverride: String? = null) {
        val textToSend = (textOverride ?: _uiState.value.messageText).trim()
        val username = _uiState.value.username.trim()

        if (textToSend.isEmpty() || username.isEmpty() || !_uiState.value.isUsernameConfirmed) {
            return
        }

        val messageId = UUID.randomUUID().toString()
        val outgoingMessage = DiscordChatMessage(
            id = messageId,
            senderName = username,
            text = textToSend,
            timestamp = System.currentTimeMillis(),
            isFromSelf = true,
            status = MessageStatus.SENDING
        )

        _uiState.update {
            it.copy(
                messageText = if (textOverride == null) "" else it.messageText,
                isSending = true,
                messages = it.messages + outgoingMessage,
                feedbackMessage = null
            )
        }

        viewModelScope.launch {
            val result = DiscordWebhookService.sendMessage(
                username = username,
                messageText = textToSend
            )

            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        isSending = false,
                        messages = state.messages.map { msg ->
                            if (msg.id == messageId) msg.copy(status = MessageStatus.SENT) else msg
                        },
                        feedbackMessage = "Správa bola úspešne odoslaná na Discord! 🚀"
                    )
                }
            } else {
                val error = result.exceptionOrNull()?.localizedMessage ?: "Nepodarilo sa odoslať správu."
                _uiState.update { state ->
                    state.copy(
                        isSending = false,
                        messages = state.messages.map { msg ->
                            if (msg.id == messageId) msg.copy(
                                status = MessageStatus.FAILED,
                                errorMessage = error
                            ) else msg
                        },
                        feedbackMessage = "Chyba pri odosielaní: $error"
                    )
                }
            }
        }
    }

    fun clearFeedback() {
        _uiState.update { it.copy(feedbackMessage = null) }
    }
}
