package com.ssn.app.ui.training_following.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.data.api.response.TrainingFollowingResponse
import com.ssn.app.model.TrainingFollowing
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrainingFollowingIndexViewModel : ViewModel() {

    private val _trainingFollowingIndexViewState: Channel<UiState<List<TrainingFollowing>>> =
        Channel()
    val trainingFollowingIndexViewState = _trainingFollowingIndexViewState.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading())

    fun getTrainingList() {
        viewModelScope.launch {
            _trainingFollowingIndexViewState.send(UiState.Loading())
            ApiClient.getApiService().safeCall(
                onEndpoint = { getFollowingTrainingList() },
                onSuccess = { response ->
                    val trainingFollowingList =
                        response.data?.map(TrainingFollowingResponse::asDomain).orEmpty()
                    launch {
                        _trainingFollowingIndexViewState.send(UiState.Success(trainingFollowingList))
                    }
                },
                onError = { throwable ->
                    launch { _trainingFollowingIndexViewState.send(UiState.Error(throwable)) }
                }
            )
        }
    }
}
