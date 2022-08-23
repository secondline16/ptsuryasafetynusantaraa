package com.ssn.app.ui.training.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.api.response.TrainingResponse
import com.ssn.app.model.Training
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrainingIndexViewModel : ViewModel() {

    private val _trainingIndexViewState: Channel<UiState<List<Training>>> = Channel()
    val trainingIndexViewState = _trainingIndexViewState.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading())

    fun getTrainingList() = viewModelScope.launch {
        _trainingIndexViewState.send(UiState.Loading())
        ApiClient.getApiService().getTrainingList().fetchResult(
            onSuccess = { response ->
                val trainingList = response.data?.map(TrainingResponse::asDomain).orEmpty()
                _trainingIndexViewState.send(UiState.Success(trainingList))
            },
            onError = { throwable ->
                _trainingIndexViewState.send(UiState.Error(throwable))
            }
        )
    }
}
