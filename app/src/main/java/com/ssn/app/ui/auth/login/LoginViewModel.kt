package com.ssn.app.ui.auth.login

import androidx.lifecycle.ViewModel
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.preference.SharedPrefProvider
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel : ViewModel() {

    private val _loginViewState: Channel<UiState<String>> = Channel()
    val loginViewState = _loginViewState.receiveAsFlow()

    fun login(
        email: String,
        password: String
    ) {
        _loginViewState.trySend(UiState.Loading())
        ApiClient.getApiService().login(email, password).fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _loginViewState.trySend(UiState.Error(MissingResultException()))
                } else {
                    val user = response.data.asDomain()
                    SharedPrefProvider.setIsLogin(true)
                    SharedPrefProvider.saveUser(user)
                    _loginViewState.trySend(UiState.Success(response.meta?.message.orEmpty()))
                }
            },
            onError = { throwable ->
                _loginViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
