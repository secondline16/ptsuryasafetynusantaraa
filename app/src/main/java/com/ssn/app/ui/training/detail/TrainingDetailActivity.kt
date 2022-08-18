package com.ssn.app.ui.training.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.ssn.app.R
import com.ssn.app.databinding.ActivityTrainingDetailBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.orDash
import com.ssn.app.extension.showSnackBar
import com.ssn.app.extension.toCurrencyFormat
import com.ssn.app.helper.DateHelper
import com.ssn.app.model.TrainingDetail
import com.ssn.app.ui.training.register.TrainingRegisterActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class TrainingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingDetailBinding
    private val viewModel: TrainingDetailViewModel by viewModels()
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        processIntentExtras()
        if (savedInstanceState == null) {
            viewModel.getTrainingDetail(id)
        }
        initObserve()
        initListener()
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.apply {
            title = getString(R.string.label_detail_pelatihan)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun initListener() = with(binding) {
        btnRegisterTraining.setOnClickListener {
            navigateToRegisterTraining()
        }
    }

    private fun navigateToRegisterTraining() {
        TrainingRegisterActivity.start(this@TrainingDetailActivity, id)
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.trainingDetailViewState.collect { viewState ->
                    handleTrainingDetailViewState(viewState)
                }
            }
        }
    }

    private fun handleTrainingDetailViewState(uiState: UiState<TrainingDetail>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> bindData(uiState.data)
        }
    }

    private fun bindData(data: TrainingDetail) = with(binding) {
        ivTraining.load(data.trainingImage)
        tvTrainingName.text = data.trainingName.orDash()
        tvTrainingDetail.text = data.trainingDescription.orDash()
        tvTrainingPrice.text = data.trainingPrice.toCurrencyFormat()
        tvTrainingStatus.text = if (data.trainingStatus) {
            getString(R.string.label_sudah_pernah_daftar_pelatihan)
        } else {
            getString(R.string.label_belum_pernah_daftar_pelatihan)
        }
        btnRegisterTraining.isVisible = !data.trainingStatus
        val startDate = DateHelper.changeFormat(
            dateString = data.trainingStartDate,
            oldFormat = DateHelper.API_DATE_PATTERN,
            newFormat = DateHelper.VIEW_DATE_PATTERN
        )
        val endDate = DateHelper.changeFormat(
            dateString = data.trainingEndDate,
            oldFormat = DateHelper.API_DATE_PATTERN,
            newFormat = DateHelper.VIEW_DATE_PATTERN
        )
        tvTrainingSchedule.text = root.context.getString(
            R.string.formatter_dash_divider,
            startDate,
            endDate
        )
    }

    private fun processIntentExtras() {
        id = intent.getIntExtra(EXTRA_ID, 0)
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: Int) {
            context.openActivity(TrainingDetailActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }
    }
}
