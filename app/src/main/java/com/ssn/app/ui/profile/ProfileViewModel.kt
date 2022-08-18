package com.ssn.app.ui.profile

import com.ssn.app.common.BaseViewModel
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.preference.SharedPrefProvider
import com.ssn.app.model.User
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel : BaseViewModel() {

    private val _profileViewState: Channel<UiState<User>> = Channel(capacity = 1)
    val profileViewState = _profileViewState.receiveAsFlow()

    private val _logoutViewState: Channel<UiState<Any?>> = Channel(capacity = 1)
    val logoutViewState = _logoutViewState.receiveAsFlow()

    fun getProfile(fetchNetwork: Boolean = true) {
        val userCache = SharedPrefProvider.getUser()
        if (!fetchNetwork) {
            _profileViewState.trySend(UiState.Success(userCache))
            return
        }
        _profileViewState.trySend(UiState.Loading(userCache))
        ApiClient.getApiService().getProfile().fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _profileViewState.trySend(UiState.Error(MissingResultException()))
                } else {
                    val user = response.data.asDomain()
                    SharedPrefProvider.saveUser(user)
                    _messageChannel.trySend(response.meta?.message.orEmpty())
                    _profileViewState.trySend(UiState.Success(user))
                }
            },
            onError = { throwable ->
                _profileViewState.trySend(UiState.Error(throwable))
            }
        )
    }

    fun updateAvatar(file: File) {
        val multipartBodyPart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            file.asRequestBody()
        )
        _profileViewState.trySend(UiState.Loading())
        ApiClient.getApiService().updateAvatar(multipartBodyPart).fetchResult(
            onSuccess = { response ->
                _messageChannel.trySend(response.meta?.message.orEmpty())
                val updatedUser = SharedPrefProvider.getUser().copy(photo = response.data.orEmpty())
                SharedPrefProvider.saveUser(updatedUser)
                _profileViewState.trySend(UiState.Success(updatedUser))
            },
            onError = { throwable ->
                _messageChannel.trySend(throwable.message.orEmpty())
            }
        )
    }

    fun logout() {
        _logoutViewState.trySend(UiState.Loading())
        SharedPrefProvider.clearAppPref()
        _logoutViewState.trySend(UiState.Success(null))
    }

    init {
        getProfile()
    }
}
