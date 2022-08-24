package com.ssn.app.ui.training.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.model.TrainingDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TrainingDetailViewModel : ViewModel() {

    private val _trainingDetailViewState: Channel<UiState<TrainingDetail>> = Channel(capacity = 1)
    val trainingDetailViewState = _trainingDetailViewState.receiveAsFlow()

    fun getTrainingDetail(id: Int) = viewModelScope.launch {
        _trainingDetailViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { getTrainingDetail(id) },
            onSuccess = { response ->
                if (response.data == null) {
                    _trainingDetailViewState.send(UiState.Error(MissingResultException()))
                } else {
                    _trainingDetailViewState.send(UiState.Success(response.data.asDomain()))
                }
            },
            onError = { throwable ->
                _trainingDetailViewState.send(UiState.Error(throwable))
            }
        )
    }
}
