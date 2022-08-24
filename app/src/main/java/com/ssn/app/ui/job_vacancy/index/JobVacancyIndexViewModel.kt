package com.ssn.app.ui.job_vacancy.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.data.api.response.JobVacancyResponse
import com.ssn.app.model.JobVacancy
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JobVacancyIndexViewModel : ViewModel() {

    private val _jobVacancyIndexViewState: Channel<UiState<List<JobVacancy>>> = Channel()
    val jobVacancyIndexViewState = _jobVacancyIndexViewState.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading())

    fun getJobVacancyList() = viewModelScope.launch {
        _jobVacancyIndexViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { getJobVacancyList() },
            onSuccess = { response ->
                val jobVacancyList = response.data?.map(JobVacancyResponse::asDomain).orEmpty()
                _jobVacancyIndexViewState.send(UiState.Success(jobVacancyList))
            },
            onError = { throwable ->
                _jobVacancyIndexViewState.send(UiState.Error(throwable))
            }
        )
    }
}
