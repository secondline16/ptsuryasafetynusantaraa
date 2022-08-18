package com.ssn.app.ui.auth.register

import androidx.lifecycle.ViewModel
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.api.request.RegisterRequest
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RegisterViewModel : ViewModel() {

    private val _registerViewState: Channel<UiState<String>> = Channel()
    val registerViewState = _registerViewState.receiveAsFlow()

    fun register(registerRequest: RegisterRequest) {
        _registerViewState.trySend(UiState.Loading())
        ApiClient.getApiService().register(registerRequest).fetchResult(
            onSuccess = { response ->
                _registerViewState.trySend(UiState.Success(response.meta?.message.orEmpty()))
            },
            onError = { throwable ->
                _registerViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
