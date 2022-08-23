package com.ssn.app.ui.job_vacancy.index

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.app.R
import com.ssn.app.databinding.FragmentJobVacancyIndexBinding
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.viewBinding
import com.ssn.app.model.JobVacancy
import com.ssn.app.ui.job_vacancy.detail.JobVacancyDetailActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class JobVacancyIndexFragment : Fragment(R.layout.fragment_job_vacancy_index) {

    private val binding by viewBinding(FragmentJobVacancyIndexBinding::bind)
    private val viewModel: JobVacancyIndexViewModel by viewModels()
    private val jobVacancyAdapter: JobVacancyAdapter by lazy {
        JobVacancyAdapter { jobVacancy -> navigateToJobVacancyDetail(jobVacancy.id) }
    }
    private var isRefresh: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserve()
        initListener()
        fetchData()
    }

    private fun initListener() {
        binding.root.setOnRefreshListener {
            fetchData(refresh = true)
        }
    }

    private fun fetchData(refresh: Boolean = false) {
        isRefresh = refresh
        viewModel.getJobVacancyList()
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.jobVacancyIndexViewState.collect { viewState ->
                    handleJobVacancyViewState(viewState)
                }
            }
        }
    }

    private fun handleJobVacancyViewState(uiState: UiState<List<JobVacancy>>) {
        val isUiStateLoading = uiState is UiState.Loading
        if (isRefresh) showRefreshLoading(isUiStateLoading) else showLoading(isUiStateLoading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> jobVacancyAdapter.submitList(uiState.data)
        }
    }

    private fun showRefreshLoading(show: Boolean) {
        binding.root.isRefreshing = show
        isRefresh = show
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        rvJobVacancy.isVisible = !show
    }

    private fun initRecyclerView() {
        binding.rvJobVacancy.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobVacancyAdapter
        }
    }

    private fun navigateToJobVacancyDetail(id: Int) {
        JobVacancyDetailActivity.start(requireContext(), id)
    }
}
