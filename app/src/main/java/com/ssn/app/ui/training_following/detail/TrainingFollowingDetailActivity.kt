package com.ssn.app.ui.training_following.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.permissionx.guolindev.PermissionX
import com.ssn.app.R
import com.ssn.app.databinding.ActivityTrainingFollowingDetailBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.orDash
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.FileHelper.open
import com.ssn.app.helper.Helper.handleDownload
import com.ssn.app.helper.Helper.storage
import com.ssn.app.model.TrainingFollowingDetail
import com.ssn.app.ui.profile.edit.EditProfileActivity
import com.ssn.app.ui.training_following.upload_certification.TrainingUploadCertificationActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class TrainingFollowingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingFollowingDetailBinding
    private val viewModel: TrainingFollowingDetailViewModel by viewModels()
    private var id: Int = 0

    private var trainingFollowingDetail: TrainingFollowingDetail? = null

    private val activityResultContracts = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult? ->
        if (activityResult == null) return@registerForActivityResult
        if (activityResult.resultCode == TrainingUploadCertificationActivity.RESULT_CODE_SUCCESS) {
            viewModel.getTrainingFollowingDetail(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingFollowingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        processIntentExtras()
        initObserve()
        initListener()
        viewModel.getTrainingFollowingDetail(id)
    }

    private fun initListener() = with(binding) {
        btnTrainingBook.setOnClickListener {
            downloadTrainingBook()
        }

        btnTrainerCv.setOnClickListener {
            downloadCvTrainer()
        }

        btnWhatsappGroup.setOnClickListener {
            openWhatsappGroup()
        }

        btnTrainingCertification.setOnClickListener {
            downloadTrainingCertificate()
        }

        btnCompetencyCertification.setOnClickListener {
            downloadCompetencyCertificate()
        }

        btnUploadCertificationRequirement.setOnClickListener {
            navigateToUploadCertificationRequirement()
        }
    }

    private fun openWhatsappGroup() {
        val whatsappUri = trainingFollowingDetail?.whatsappGroup.orEmpty()
        if (whatsappUri.isEmpty()) return
        val destinationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUri))
        Intent.createChooser(destinationIntent, "Open").also { intent ->
            startActivity(intent)
        }
    }

    private fun downloadCompetencyCertificate() = with(binding) {
        val url = trainingFollowingDetail?.competenceCertificate.orEmpty()
        lifecycleScope.handleDownload(
            url = url,
            onWaiting = {
                btnCompetencyCertification.setState(
                    false,
                    getString(R.string.label_waiting)
                )
            },
            onDownloading = { progress ->
                val progressStr = "${progress.downloadSizeStr()} / ${progress.totalSizeStr()}"
                btnCompetencyCertification.setState(false, progressStr)
            },
            onSuccess = { file ->
                btnCompetencyCertification.setState(
                    true,
                    getString(R.string.label_sertifikasi_kompetensi)
                )
                checkStoragePermission { file?.open(this@TrainingFollowingDetailActivity) }
            },
            onFailed = { root.showSnackBar("Failed to download") },
            onError = { throwable ->
                root.showSnackBar("Error : ${throwable.message}")
            }
        )
    }

    private fun downloadTrainingCertificate() = with(binding) {
        val url = trainingFollowingDetail?.trainingCertificate.orEmpty()
        lifecycleScope.handleDownload(
            url = url,
            onWaiting = {
                btnTrainingCertification.setState(
                    false,
                    getString(R.string.label_waiting)
                )
            },
            onDownloading = { progress ->
                val progressStr = "${progress.downloadSizeStr()} / ${progress.totalSizeStr()}"
                btnTrainingCertification.setState(false, progressStr)
            },
            onSuccess = { file ->
                btnTrainingCertification.setState(
                    true,
                    getString(R.string.label_sertifikasi_pelatihan)
                )
                checkStoragePermission { file?.open(this@TrainingFollowingDetailActivity) }
            },
            onFailed = { root.showSnackBar("Failed to download") },
            onError = { throwable ->
                root.showSnackBar("Error : ${throwable.message}")
            }
        )
    }

    private fun downloadCvTrainer() = with(binding) {
        val url = trainingFollowingDetail?.trainerCv.orEmpty()
        lifecycleScope.handleDownload(
            url = url,
            onWaiting = { btnTrainerCv.setState(false, getString(R.string.label_waiting)) },
            onDownloading = { progress ->
                val progressStr = "${progress.downloadSizeStr()} / ${progress.totalSizeStr()}"
                btnTrainerCv.setState(false, progressStr)
            },
            onSuccess = { file ->
                btnTrainerCv.setState(true, getString(R.string.label_cv))
                checkStoragePermission { file?.open(this@TrainingFollowingDetailActivity) }
            },
            onFailed = { root.showSnackBar("Failed to download") },
            onError = { throwable ->
                root.showSnackBar("Error : ${throwable.message}")
            }
        )
    }

    private fun checkStoragePermission(action: () -> Unit) {
        PermissionX.init(this).storage { action.invoke() }
    }

    private fun Button.setState(enable: Boolean, title: String) {
        isEnabled = enable
        text = title
    }

    private fun downloadTrainingBook() = with(binding) {
        val url = trainingFollowingDetail?.trainingBook.orEmpty()
        lifecycleScope.handleDownload(
            url = url,
            onWaiting = { btnTrainingBook.setState(false, getString(R.string.label_waiting)) },
            onDownloading = { progress ->
                val progressStr = "${progress.downloadSizeStr()} / ${progress.totalSizeStr()}"
                btnTrainingBook.setState(false, progressStr)
            },
            onSuccess = { file ->
                btnTrainingBook.setState(true, getString(R.string.label_materi_pelatihan))
                checkStoragePermission { file?.open(this@TrainingFollowingDetailActivity) }
            },
            onFailed = { root.showSnackBar("Failed to download") },
            onError = { throwable ->
                root.showSnackBar("Error : ${throwable.message}")
            }
        )
    }

    private fun navigateToUploadCertificationRequirement() {
        activityResultContracts.launch(TrainingUploadCertificationActivity.getIntent(this))
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
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
