package com.ssn.app.ui.job_vacancy.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.model.JobVacancyDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class JobVacancyDetailViewModel : ViewModel() {

    private val _jobVacancyDetailViewState: Channel<UiState<JobVacancyDetail>> = Channel(capacity = 1)
    val jobVacancyDetailViewState = _jobVacancyDetailViewState.receiveAsFlow()

    fun getJobVacancyDetail(id: Int) {
        _jobVacancyDetailViewState.trySend(UiState.Loading())
        ApiClient.getApiService().getJobVacancyDetail(id).fetchResult(
            onSuccess = { response ->
                if (response.data == null) {
                    _jobVacancyDetailViewState.trySend(UiState.Error(MissingResultException()))
                } else {
                    _jobVacancyDetailViewState.trySend(UiState.Success(response.data.asDomain()))
                }
            },
            onError = { throwable ->
                _jobVacancyDetailViewState.trySend(UiState.Error(throwable))
            }
        )
    }
}
