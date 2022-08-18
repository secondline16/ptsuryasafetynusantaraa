package com.ssn.app.ui.training.register

import androidx.lifecycle.ViewModel
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TrainingRegisterViewModel : ViewModel() {

    private val _registerTrainingViewState: Channel<UiState<String>> = Channel(capacity = 1)
    val registerTrainingViewState = _registerTrainingViewState.receiveAsFlow()

    fun registerTraining(id: Int, invoice: File) {
        val trainingId = id.toString().toRequestBody()
        val invoiceMultipart = MultipartBody.Part.createFormData(
            "invoice_proof",
            invoice.name,
            invoice.asRequestBody()
        )
        _registerTrainingViewState.trySend(UiState.Loading())
        ApiClient.getApiService().registerTraining(trainingId, invoiceMultipart).fetchResult(
            onSuccess = { response ->
                _registerTrainingViewState.trySend(UiState.Success(response.meta?.message.orEmpty()))
            },
            onError = { throwable ->
                _registerTrainingViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
