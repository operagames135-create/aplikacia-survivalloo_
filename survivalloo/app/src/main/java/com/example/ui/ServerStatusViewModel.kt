package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.MinecraftServerStatus
import com.example.model.ServerDataRepository
import com.example.model.ServerStatusUiState
import com.example.network.MinecraftStatusService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ServerStatusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ServerStatusUiState>(ServerStatusUiState.Loading)
    val uiState: StateFlow<ServerStatusUiState> = _uiState.asStateFlow()

    private val _selectedAddress = MutableStateFlow(ServerDataRepository.JAVA_IP)
    val selectedAddress: StateFlow<String> = _selectedAddress.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        loadServerStatus()
        startAutoRefreshLoop()
    }

    fun selectAddress(address: String) {
        if (_selectedAddress.value != address) {
            _selectedAddress.value = address
            loadServerStatus(isManualRefresh = true)
        }
    }

    fun refreshStatus() {
        loadServerStatus(isManualRefresh = true)
    }

    private fun loadServerStatus(isManualRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isManualRefresh) {
                _isRefreshing.value = true
            } else if (_uiState.value !is ServerStatusUiState.Success) {
                _uiState.value = ServerStatusUiState.Loading
            }

            val address = _selectedAddress.value
            val result = MinecraftStatusService.fetchServerStatus(address)

            result.onSuccess { status ->
                if (status.online) {
                    _uiState.value = ServerStatusUiState.Success(status)
                } else {
                    _uiState.value = ServerStatusUiState.Offline(
                        address = address,
                        message = "Server je momentálne offline na Aternose."
                    )
                }
            }.onFailure { error ->
                _uiState.value = ServerStatusUiState.Offline(
                    address = address,
                    message = error.localizedMessage ?: "Nepodarilo sa overiť status servera."
                )
            }

            _isRefreshing.value = false
        }
    }

    private fun startAutoRefreshLoop() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(40_000) // refresh every 40 seconds
                loadServerStatus(isManualRefresh = false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoRefreshJob?.cancel()
    }
}
