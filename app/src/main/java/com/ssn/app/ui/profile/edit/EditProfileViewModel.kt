package com.ssn.app.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.api.request.EditProfileRequest
import com.ssn.app.data.preference.SharedPrefProvider
import com.ssn.app.model.User
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditProfileViewModel : ViewModel() {

    private val _editProfileViewState: Channel<UiState<String>> = Channel(capacity = 1)
    val editProfileViewState = _editProfileViewState.receiveAsFlow()

    fun getProfile(): User = SharedPrefProvider.getUser()

    fun editProfile(editProfileRequest: EditProfileRequest) = viewModelScope.launch {
        _editProfileViewState.send(UiState.Loading())
        ApiClient.getApiService().updateProfile(editProfileRequest).fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _editProfileViewState.send(UiState.Error(MissingResultException()))
                } else {
                    val user = response.data.asDomain()
                    SharedPrefProvider.saveUser(user)
                    _editProfileViewState.send(UiState.Success(response.meta?.message.orEmpty()))
                }
            },
            onError = { throwable ->
                _editProfileViewState.send(UiState.Error(throwable))
            }
        )
    }
}
