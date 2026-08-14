package com.example.network

import com.example.model.MinecraftServerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object MinecraftStatusService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches Minecraft Server Status for the given address.
     * Primary: api.mcstatus.io
     * Fallback: api.mcsrvstat.us
     */
    suspend fun fetchServerStatus(address: String): Result<MinecraftServerStatus> = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()

        // 1. Try MCStatus.io API
        val mcStatusResult = queryMcStatusIo(address, startTime)
        if (mcStatusResult.isSuccess) {
            return@withContext mcStatusResult
        }

        // 2. Try MCSrvStat.us API fallback
        val mcSrvStatResult = queryMcSrvStat(address, startTime)
        if (mcSrvStatResult.isSuccess) {
            return@withContext mcSrvStatResult
        }

        // If both failed with network errors, return a clean offline state with error info
        val parts = address.split(":")
        val host = parts[0]
        val port = parts.getOrNull(1)?.toIntOrNull() ?: 25565

        Result.success(
            MinecraftServerStatus(
                online = false,
                host = host,
                port = port,
                playersOnline = 0,
                maxPlayers = 20,
                version = "1.8 – 1.26.2",
                motd = "Server je offline (Aternos)",
                latencyMs = null,
                lastCheckedMillis = System.currentTimeMillis(),
                errorMessage = mcStatusResult.exceptionOrNull()?.message
            )
        )
    }

    private fun queryMcStatusIo(address: String, startTime: Long): Result<MinecraftServerStatus> {
        return try {
            val url = "https://api.mcstatus.io/v2/status/java/$address"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "SurvivalLoo-Android-App")
                .build()

            httpClient.newCall(request).execute().use { response ->
                val latency = System.currentTimeMillis() - startTime
                if (!response.isSuccessful) {
                    return Result.failure(Exception("HTTP ${response.code} from mcstatus.io"))
                }

                val bodyString = response.body?.string() ?: return Result.failure(Exception("Empty body"))
                val json = JSONObject(bodyString)

                val online = json.optBoolean("online", false)
                val host = json.optString("host", address.substringBefore(":"))
                val port = json.optInt("port", address.substringAfter(":", "25565").toIntOrNull() ?: 25565)

                if (!online) {
                    return Result.success(
                        MinecraftServerStatus(
                            online = false,
                            host = host,
                            port = port,
                            playersOnline = 0,
                            maxPlayers = 20,
                            version = "1.8 – 1.26.2",
                            motd = "Server je offline",
                            latencyMs = latency,
                            lastCheckedMillis = System.currentTimeMillis()
                        )
                    )
                }

                val playersObj = json.optJSONObject("players")
                val playersOnline = playersObj?.optInt("online", 0) ?: 0
                val maxPlayers = playersObj?.optInt("max", 20) ?: 20
                val playerList = mutableListOf<String>()
                val listArray = playersObj?.optJSONArray("list")
                if (listArray != null) {
                    for (i in 0 until listArray.length()) {
                        val p = listArray.optJSONObject(i)
                        val name = p?.optString("name_clean") ?: p?.optString("name_raw")
                        if (!name.isNullOrBlank()) {
                            playerList.add(name)
                        }
                    }
                }

                val versionObj = json.optJSONObject("version")
                val versionName = versionObj?.optString("name_clean") ?: versionObj?.optString("name_raw") ?: "1.8 – 1.26.2"

                val motdObj = json.optJSONObject("motd")
                val motd = motdObj?.optString("clean") ?: motdObj?.optString("raw") ?: "SurvivalLoo Server"

                Result.success(
                    MinecraftServerStatus(
                        online = true,
                        host = host,
                        port = port,
                        playersOnline = playersOnline,
                        maxPlayers = maxPlayers,
                        playerList = playerList,
                        version = versionName,
                        motd = motd.trim(),
                        latencyMs = latency,
                        lastCheckedMillis = System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun queryMcSrvStat(address: String, startTime: Long): Result<MinecraftServerStatus> {
        return try {
            val url = "https://api.mcsrvstat.us/3/$address"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "SurvivalLoo-Android-App")
                .build()

            httpClient.newCall(request).execute().use { response ->
                val latency = System.currentTimeMillis() - startTime
                if (!response.isSuccessful) {
                    return Result.failure(Exception("HTTP ${response.code} from mcsrvstat.us"))
                }

                val bodyString = response.body?.string() ?: return Result.failure(Exception("Empty body"))
                val json = JSONObject(bodyString)

                val online = json.optBoolean("online", false)
                val host = json.optString("hostname", address.substringBefore(":"))
                val port = json.optInt("port", address.substringAfter(":", "25565").toIntOrNull() ?: 25565)

                if (!online) {
                    return Result.success(
                        MinecraftServerStatus(
                            online = false,
                            host = host,
                            port = port,
                            playersOnline = 0,
                            maxPlayers = 20,
                            version = "1.8 – 1.26.2",
                            motd = "Server je offline",
                            latencyMs = latency,
                            lastCheckedMillis = System.currentTimeMillis()
                        )
                    )
                }

                val playersObj = json.optJSONObject("players")
                val playersOnline = playersObj?.optInt("online", 0) ?: 0
                val maxPlayers = playersObj?.optInt("max", 20) ?: 20
                val playerList = mutableListOf<String>()
                val listArray = playersObj?.optJSONArray("list")
                if (listArray != null) {
                    for (i in 0 until listArray.length()) {
                        val p = listArray.optJSONObject(i)
                        val name = p?.optString("name")
                        if (!name.isNullOrBlank()) {
                            playerList.add(name)
                        }
                    }
                }

                val version = json.optString("version", "1.8 – 1.26.2")

                val motdObj = json.optJSONObject("motd")
                val motdArray = motdObj?.optJSONArray("clean")
                val motd = if (motdArray != null && motdArray.length() > 0) {
                    (0 until motdArray.length()).joinToString("\n") { motdArray.getString(it) }
                } else {
                    "SurvivalLoo Minecraft Server"
                }

                Result.success(
                    MinecraftServerStatus(
                        online = true,
                        host = host,
                        port = port,
                        playersOnline = playersOnline,
                        maxPlayers = maxPlayers,
                        playerList = playerList,
                        version = version,
                        motd = motd.trim(),
                        latencyMs = latency,
                        lastCheckedMillis = System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
