package com.ssn.app.ui.training_following.detail

import androidx.lifecycle.ViewModel
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.model.TrainingFollowingDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class TrainingFollowingDetailViewModel : ViewModel() {

    private val _trainingFollowingDetailViewState: Channel<UiState<TrainingFollowingDetail>> = Channel(capacity = 1)
    val trainingFollowingDetailViewState = _trainingFollowingDetailViewState.receiveAsFlow()

    fun getTrainingFollowingDetail(id: Int) {
        _trainingFollowingDetailViewState.trySend(UiState.Loading())
        ApiClient.getApiService().getFollowingTrainingDetail(id).fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _trainingFollowingDetailViewState.trySend(UiState.Error(MissingResultException()))
                } else {
                    _trainingFollowingDetailViewState.trySend(UiState.Success(response.data.asDomain()))
                }
            },
            onError = { throwable ->
                _trainingFollowingDetailViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
