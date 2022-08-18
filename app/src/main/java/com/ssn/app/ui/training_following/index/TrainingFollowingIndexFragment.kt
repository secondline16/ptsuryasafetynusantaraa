package com.ssn.app.ui.training_following.index

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
import com.ssn.app.databinding.FragmentTrainingFollowingIndexBinding
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.viewBinding
import com.ssn.app.model.TrainingFollowing
import com.ssn.app.ui.training_following.detail.TrainingFollowingDetailActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class TrainingFollowingIndexFragment : Fragment(R.layout.fragment_training_following_index) {

    private val binding by viewBinding(FragmentTrainingFollowingIndexBinding::bind)
    private val viewModel: TrainingFollowingIndexViewModel by viewModels()
    private val trainingFollowingAdapter: TrainingFollowingAdapter by lazy {
        TrainingFollowingAdapter { trainingFollowing ->
            navigateToTrainingFollowingDetail(trainingFollowing.id)
        }
    }
    private var isRefresh: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.root.setOnRefreshListener {
            isRefresh = true
            viewModel.getTrainingList()
        }
    }

    private fun initRecyclerView() {
        binding.rvTrainingFollowing.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trainingFollowingAdapter
        }
    }

    private fun navigateToTrainingFollowingDetail(id: Int) {
        TrainingFollowingDetailActivity.start(requireContext(), id)
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.trainingFollowingIndexViewState.collect { viewState ->
                    handleTrainingFollowingViewState(viewState)
                }
            }
        }
    }

    private fun handleTrainingFollowingViewState(uiState: UiState<List<TrainingFollowing>>) {
        val isUiStateLoading = uiState is UiState.Loading
        if (isRefresh) showRefreshLoading(isUiStateLoading) else showLoading(isUiStateLoading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> trainingFollowingAdapter.submitList(uiState.data)
        }
    }

    private fun showRefreshLoading(show: Boolean) {
        binding.root.isRefreshing = show
        isRefresh = show
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        rvTrainingFollowing.isVisible = !show
    }
}
