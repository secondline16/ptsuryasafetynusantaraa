package com.ssn.app.ui.job_vacancy.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssn.app.R
import com.ssn.app.databinding.ActivityJobVacancyDetailBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.orDash
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.DateHelper
import com.ssn.app.model.JobVacancyDetail
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class JobVacancyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobVacancyDetailBinding
    private val viewModel: JobVacancyDetailViewModel by viewModels()
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobVacancyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        processIntentExtras()
        if (savedInstanceState == null) {
            viewModel.getJobVacancyDetail(id)
        }
        initObserve()
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.apply {
            title = getString(R.string.label_detail_lowongan)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.jobVacancyDetailViewState.collect { viewState ->
                    handleJobVacancyDetailViewState(viewState)
                }
            }
        }
    }

    private fun handleJobVacancyDetailViewState(uiState: UiState<JobVacancyDetail>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> bindData(uiState.data)
        }
    }

    private fun bindData(data: JobVacancyDetail) = with(binding) {
        tvJobVacancy.text = data.jobPosition.orDash()
        tvCompanyName.text = data.companyName.orDash()
        tvDetail.text = data.description.orDash()
        tvRequirement.text = data.requirements.orDash()
        val deadlineDate = DateHelper.changeFormat(
            dateString = data.deadline,
            oldFormat = DateHelper.API_DATE_PATTERN,
            newFormat = DateHelper.VIEW_DATE_PATTERN
        )
        tvDeadline.text = root.context.getString(
            R.string.formatter_deadline,
            deadlineDate
        )
        tvDeadline.text = deadlineDate.orDash()
    }

    private fun processIntentExtras() {
        id = intent.getIntExtra(EXTRA_ID, 0)
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: Int) {
            context.openActivity(JobVacancyDetailActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }
    }
}