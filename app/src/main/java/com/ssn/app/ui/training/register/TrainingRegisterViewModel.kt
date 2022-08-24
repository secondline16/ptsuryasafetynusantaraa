package com.ssn.app.ui.training.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TrainingRegisterViewModel : ViewModel() {

    private val _registerTrainingViewState: Channel<UiState<String>> = Channel(capacity = 1)
    val registerTrainingViewState = _registerTrainingViewState.receiveAsFlow()

    fun registerTraining(id: Int, invoice: File) = viewModelScope.launch {
        val trainingId = id.toString().toRequestBody()
        val invoiceMultipart = MultipartBody.Part.createFormData(
            "invoice_proof",
            invoice.name,
            invoice.asRequestBody()
        )
        _registerTrainingViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { registerTraining(trainingId, invoiceMultipart) },
            onSuccess = { response ->
                _registerTrainingViewState.send(UiState.Success(response.meta?.message.orEmpty()))
            },
            onError = { throwable ->
                _registerTrainingViewState.send(UiState.Error(throwable))
            }
        )
    }
}
