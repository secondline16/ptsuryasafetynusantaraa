package com.ssn.app.ui.training_following.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.model.TrainingFollowingDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TrainingFollowingDetailViewModel : ViewModel() {

    private val _trainingFollowingDetailViewState: Channel<UiState<TrainingFollowingDetail>> =
        Channel(capacity = 1)
    val trainingFollowingDetailViewState = _trainingFollowingDetailViewState.receiveAsFlow()

    fun getTrainingFollowingDetail(id: Int) = viewModelScope.launch {
        _trainingFollowingDetailViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { getFollowingTrainingDetail(id) },
            onSuccess = { response ->
                if (response.data == null) {
                    _trainingFollowingDetailViewState.send(UiState.Error(MissingResultException()))
                } else {
                    _trainingFollowingDetailViewState.send(UiState.Success(response.data.asDomain()))
                }
            },
            onError = { throwable ->
                _trainingFollowingDetailViewState.send(UiState.Error(throwable))
            }
        )
    }
}
