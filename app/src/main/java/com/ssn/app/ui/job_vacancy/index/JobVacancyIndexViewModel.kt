package com.ssn.app.ui.job_vacancy.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.fetchResult
import com.ssn.app.data.api.response.JobVacancyResponse
import com.ssn.app.model.JobVacancy
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class JobVacancyIndexViewModel : ViewModel() {

    private val _jobVacancyIndexViewState: Channel<UiState<List<JobVacancy>>> = Channel()
    val jobVacancyIndexViewState = _jobVacancyIndexViewState.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading())

    fun getJobVacancyList() {
        _jobVacancyIndexViewState.trySend(UiState.Loading())
        ApiClient.getApiService().getJobVacancyList().fetchResult(
            onSuccess = { response ->
                val jobVacancyList = response.data?.map(JobVacancyResponse::asDomain).orEmpty()
                _jobVacancyIndexViewState.trySend(UiState.Success(jobVacancyList))
            },
            onError = { throwable ->
                _jobVacancyIndexViewState.trySend(UiState.Error(throwable))
            }
        )
    }

    init {
        getJobVacancyList()
    }
}
