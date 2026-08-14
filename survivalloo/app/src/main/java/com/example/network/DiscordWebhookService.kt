package com.example.network

import com.example.model.ServerDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object DiscordWebhookService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    /**
     * Sends a message to the Discord channel via the configured Webhook.
     */
    suspend fun sendMessage(
        username: String,
        messageText: String,
        webhookUrl: String = ServerDataRepository.DISCORD_WEBHOOK_URL
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cleanUsername = username.trim().ifEmpty { "Hráč SurvivalLoo" }
            val cleanMessage = messageText.trim()

            if (cleanMessage.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Správa nemôže byť prázdna"))
            }

            val jsonPayload = JSONObject().apply {
                put("username", "$cleanUsername [SurvivalLoo App]")
                put("content", cleanMessage)
                // Minecraft player avatar for nice visual display in Discord
                put("avatar_url", "https://mc-heads.net/avatar/${cleanUsername.replace(" ", "_")}/128")
            }

            val body = jsonPayload.toString().toRequestBody(JSON_MEDIA_TYPE)
            val request = Request.Builder()
                .url(webhookUrl)
                .post(body)
                .header("User-Agent", "SurvivalLoo-Android-App")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful || response.code == 204 || response.code == 200) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.body?.string()
                    Result.failure(Exception("Chyba Discordu (${response.code}): ${errorBody ?: "Neočakávaná odpoveď"}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
