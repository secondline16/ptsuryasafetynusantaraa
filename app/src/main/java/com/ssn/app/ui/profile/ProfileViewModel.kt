package com.ssn.app.ui.profile

import androidx.lifecycle.viewModelScope
import com.ssn.app.common.BaseViewModel
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.preference.SharedPrefProvider
import com.ssn.app.model.User
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel : BaseViewModel() {

    private val _profileViewState: Channel<UiState<User>> = Channel(capacity = 1)
    val profileViewState = _profileViewState.receiveAsFlow()

    private val _logoutViewState: Channel<UiState<Any?>> = Channel(capacity = 1)
    val logoutViewState = _logoutViewState.receiveAsFlow()

    fun getProfile(fetchNetwork: Boolean = true) = viewModelScope.launch {
        val userCache = SharedPrefProvider.getUser()
        if (!fetchNetwork) {
            _profileViewState.send(UiState.Success(userCache))
            return@launch
        }
        _profileViewState.send(UiState.Loading(userCache))
        ApiClient.getApiService().getProfile().fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _profileViewState.send(UiState.Error(MissingResultException()))
                } else {
                    val user = response.data.asDomain()
                    SharedPrefProvider.saveUser(user)
                    _messageChannel.send(response.meta?.message.orEmpty())
                    _profileViewState.send(UiState.Success(user))
                }
            },
            onError = { throwable ->
                _profileViewState.send(UiState.Error(throwable))
            }
        )
    }

    fun updateAvatar(file: File) = viewModelScope.launch {
        val multipartBodyPart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            file.asRequestBody()
        )
        _profileViewState.send(UiState.Loading())
        ApiClient.getApiService().updateAvatar(multipartBodyPart).fetchResult(
            onSuccess = { response ->
                _messageChannel.send(response.meta?.message.orEmpty())
                val updatedUser = SharedPrefProvider.getUser().copy(photo = response.data.orEmpty())
                SharedPrefProvider.saveUser(updatedUser)
                _profileViewState.send(UiState.Success(updatedUser))
            },
            onError = { throwable ->
                _messageChannel.send(throwable.message.orEmpty())
            }
        )
    }

    fun logout() = viewModelScope.launch {
        _logoutViewState.send(UiState.Loading())
        SharedPrefProvider.clearAppPref()
        _logoutViewState.send(UiState.Success(null))
    }

    init {
        getProfile()
    }
}
