package com.ssn.app.ui.training_following.upload_certification

import androidx.lifecycle.ViewModel
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TrainingUploadCertificationViewModel : ViewModel() {

    private val _uploadCertificateViewState: Channel<UiState<String>> = Channel(capacity = 1)
    val uploadCertificateViewState = _uploadCertificateViewState.receiveAsFlow()

    private val _buttonEnableState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val buttonEnableState = _buttonEnableState.asStateFlow()

    fun setEnableButton(enableButton: Boolean) {
        _buttonEnableState.value = enableButton
    }

    fun uploadDocuments(
        cv: File,
        ktp: File,
        ijazah: File,
        workExperience: File,
        portfolio: File,
        optionalFile: File?
    ) = with(_uploadCertificateViewState) {
        trySend(UiState.Loading())
        ApiClient.getApiService().uploadTrainingRequirement(
            cv = cv.run { MultipartBody.Part.createFormData("cv", name, asRequestBody()) },
            ktp = ktp.run { MultipartBody.Part.createFormData("ktp", name, asRequestBody()) },
            ijazah = ijazah.run {
                MultipartBody.Part.createFormData("ijazah", name, asRequestBody())
            },
            workExperience = workExperience.run {
                MultipartBody.Part.createFormData("work_experience", name, asRequestBody())
            },
            portfolio = portfolio.run {
                MultipartBody.Part.createFormData("portfolio", name, asRequestBody())
            },
            optionalFile = optionalFile?.run {
                MultipartBody.Part.createFormData("optional_file", name, asRequestBody())
            }
        ).fetchResult(
            onSuccess = { response ->
                _uploadCertificateViewState.trySend(UiState.Success(response.meta?.message.orEmpty()))
            },
            onError = { throwable ->
                _uploadCertificateViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
