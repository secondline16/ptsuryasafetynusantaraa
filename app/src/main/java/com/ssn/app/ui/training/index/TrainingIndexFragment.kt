package com.ssn.app.ui.training.index

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
import com.ssn.app.databinding.FragmentTrainingIndexBinding
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.viewBinding
import com.ssn.app.model.Training
import com.ssn.app.ui.training.detail.TrainingDetailActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class TrainingIndexFragment : Fragment(R.layout.fragment_training_index) {

    private val binding by viewBinding(FragmentTrainingIndexBinding::bind)
    private val viewModel: TrainingIndexViewModel by viewModels()
    private val trainingAdapter: TrainingAdapter by lazy {
        TrainingAdapter { training -> navigateToTrainingDetail(training.id) }
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
        viewModel.getTrainingList()
    }

    private fun initRecyclerView() {
        binding.rvTraining.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trainingAdapter
        }
    }

    private fun navigateToTrainingDetail(id: Int) {
        TrainingDetailActivity.start(requireContext(), id)
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.trainingIndexViewState.collect { viewState ->
                    handleTrainingViewState(viewState)
                }
            }
        }
    }

    private fun handleTrainingViewState(uiState: UiState<List<Training>>) {
        val isUiStateLoading = uiState is UiState.Loading
        if (isRefresh) showRefreshLoading(isUiStateLoading) else showLoading(isUiStateLoading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> trainingAdapter.submitList(uiState.data)
        }
    }

    private fun showRefreshLoading(show: Boolean) {
        binding.root.isRefreshing = show
        isRefresh = show
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        rvTraining.isVisible = !show
    }
}
