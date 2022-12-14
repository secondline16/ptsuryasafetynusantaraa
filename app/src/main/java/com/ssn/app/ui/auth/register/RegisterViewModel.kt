package com.ssn.app.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.data.api.request.RegisterRequest
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registerViewState: Channel<UiState<String>> = Channel()
    val registerViewState = _registerViewState.receiveAsFlow()

    fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _registerViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { register(registerRequest) },
            onSuccess = { response ->
                _registerViewState.send(UiState.Success(response.meta?.message.orEmpty()))
            },
            onError = { throwable ->
                _registerViewState.send(UiState.Error(throwable))
            }
        )
    }
}
