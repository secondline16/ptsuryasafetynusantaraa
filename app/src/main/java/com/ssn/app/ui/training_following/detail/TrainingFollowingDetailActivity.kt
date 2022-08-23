package com.ssn.app.ui.training_following.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssn.app.R
import com.ssn.app.databinding.ActivityTrainingFollowingDetailBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.orDash
import com.ssn.app.extension.showSnackBar
import com.ssn.app.model.TrainingFollowingDetail
import com.ssn.app.ui.training_following.upload_certification.TrainingUploadCertificationActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class TrainingFollowingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingFollowingDetailBinding
    private val viewModel: TrainingFollowingDetailViewModel by viewModels()
    private var id: Int = 0

    private var trainingFollowingDetail: TrainingFollowingDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingFollowingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        processIntentExtras()
        if (savedInstanceState == null) {
            viewModel.getTrainingFollowingDetail(id)
        }
        initObserve()
        initListener()
    }

    private fun initListener() = with(binding) {
        btnTrainingBook.setOnClickListener {
        }

        btnTrainerCv.setOnClickListener {
        }

        btnWhatsappGroup.setOnClickListener {
        }

        btnTrainingCertification.setOnClickListener {
        }

        btnCompetencyCertification.setOnClickListener {
        }

        btnUploadCertificationRequirement.setOnClickListener {
            navigateToUploadCertificationRequirement()
        }
    }

    private fun navigateToUploadCertificationRequirement() {
        TrainingUploadCertificationActivity.start(this)
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.apply {
            title = getString(R.string.label_detail_pelatihan_diikuti)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun processIntentExtras() {
        id = intent.getIntExtra(EXTRA_ID, 0)
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.trainingFollowingDetailViewState.collect { viewState ->
                    handleTrainingFollowingDetailViewState(viewState)
                }
            }
        }
    }

    private fun handleTrainingFollowingDetailViewState(uiState: UiState<TrainingFollowingDetail>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> bindData(uiState.data)
        }
    }

    private fun bindData(data: TrainingFollowingDetail) = with(binding) {
        trainingFollowingDetail = data
        tvTrainingName.text = data.trainingName.orDash()
        tvTrainingDetail.text = data.trainingDescription.orDash()
        tvTrainingStatus.text = data.trainingStatus
        tvCertificationStatus.text = data.requirementStatus
        tvTrainerName.text = data.trainerName.orDash()
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: Int) {
            context.openActivity(TrainingFollowingDetailActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }
    }
}
