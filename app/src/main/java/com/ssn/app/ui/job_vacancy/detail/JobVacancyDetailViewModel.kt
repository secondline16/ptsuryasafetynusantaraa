package com.ssn.app.ui.job_vacancy.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.app.common.MissingResultException
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.api.config.ApiClient.safeCall
import com.ssn.app.model.JobVacancyDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class JobVacancyDetailViewModel : ViewModel() {

    private val _jobVacancyDetailViewState: Channel<UiState<JobVacancyDetail>> =
        Channel(capacity = 1)
    val jobVacancyDetailViewState = _jobVacancyDetailViewState.receiveAsFlow()

    fun getJobVacancyDetail(id: Int) = viewModelScope.launch {
        _jobVacancyDetailViewState.send(UiState.Loading())
        ApiClient.getApiService().safeCall(
            onEndpoint = { getJobVacancyDetail(id) },
            onSuccess = { response ->
                if (response.data == null) {
                    _jobVacancyDetailViewState.send(UiState.Error(MissingResultException()))
                } else {
                    _jobVacancyDetailViewState.send(UiState.Success(response.data.asDomain()))
                }
            },
            onError = { throwable ->
                _jobVacancyDetailViewState.send(UiState.Error(throwable))
            }
        )
    }
}
