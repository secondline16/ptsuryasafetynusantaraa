package com.ssn.app.ui.training_following.upload_certification

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.permissionx.guolindev.PermissionX
import com.ssn.app.R
import com.ssn.app.databinding.ActivityUploadCertificationBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.setDocumentIcon
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.FilePickerHelper
import com.ssn.app.helper.FilePickerHelper.doIfImage
import com.ssn.app.helper.FilePickerHelper.getTempFileUri
import com.ssn.app.helper.Helper.camera
import com.ssn.app.helper.Helper.showImagePickerSourceDialog
import com.ssn.app.helper.Helper.storage
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch
import java.io.File

class TrainingUploadCertificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadCertificationBinding
    private val viewModel: TrainingUploadCertificationViewModel by viewModels()
    private var pickFileType: Int? = null
    private var selectedCvUri: Uri? = null
    private var selectedKtpUri: Uri? = null
    private var selectedIjazahUri: Uri? = null
    private var selectedWorkExperienceUri: Uri? = null
    private var selectedPortfolioUri: Uri? = null
    private var selectedOptionalUri: Uri? = null

    private val pickerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult? ->
        if (activityResult == null) return@registerForActivityResult
        val selectedUri = activityResult.data?.data ?: return@registerForActivityResult
        onPickerType(
            onCvPicker = { selectedCvUri = selectedUri },
            onKtpPicker = { selectedKtpUri = selectedUri },
            onIjazahPicker = { selectedIjazahUri = selectedUri },
            onWorkExperiencePicker = { selectedWorkExperienceUri = selectedUri },
            onPortfolioPicker = { selectedPortfolioUri = selectedUri },
            onOptionalFile = { selectedOptionalUri = selectedUri }
        )
        setImagePreview()
        setEnableButton()
        pickFileType = null
    }

    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            setImagePreview()
            setEnableButton()
        }
        pickFileType = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initObserve()
        initListener()
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.apply {
            title = getString(R.string.label_upload_persyaratan_sertifikasi)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun setImagePreview() = with(binding) {
        onPickerType(
            onCvPicker = {
                selectedCvUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivCvPreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivCvPreview.apply { isVisible = true; setDocumentIcon() }
                    }
                )
            },
            onKtpPicker = {
                selectedKtpUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivKtpPreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivKtpPreview.apply { isVisible = true; setDocumentIcon() }
                    }
                )
            },
            onIjazahPicker = {
                selectedIjazahUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivIjazahPreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivIjazahPreview.apply { isVisible = true; setDocumentIcon() }
                    }
                )
            },
            onWorkExperiencePicker = {
                selectedWorkExperienceUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivWorkExperiencePreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivWorkExperiencePreview.apply {
                            isVisible = true; setDocumentIcon()
                        }
                    }
                )
            },
            onPortfolioPicker = {
                selectedPortfolioUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivPortfolioPreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivPortfolioPreview.apply {
                            isVisible = true; setDocumentIcon()
                        }
                    }
                )
            },
            onOptionalFile = {
                selectedOptionalUri.doIfImage(
                    context = this@TrainingUploadCertificationActivity,
                    action = { file ->
                        ivOptionalFilePreview.apply { isVisible = true; load(file) }
                    },
                    onNotImage = {
                        ivOptionalFilePreview.apply {
                            isVisible = true; setDocumentIcon()
                        }
                    }
                )
            }
        )
    }

    private fun onPickerType(
        onCvPicker: () -> Unit,
        onKtpPicker: () -> Unit,
        onIjazahPicker: () -> Unit,
        onWorkExperiencePicker: () -> Unit,
        onPortfolioPicker: () -> Unit,
        onOptionalFile: () -> Unit
    ) {
        when (pickFileType) {
            TYPE_CV -> onCvPicker.invoke()
            TYPE_KTP -> onKtpPicker.invoke()
            TYPE_IJAZAH -> onIjazahPicker.invoke()
            TYPE_WORK_EXPERIENCE -> onWorkExperiencePicker.invoke()
            TYPE_PORTFOLIO -> onPortfolioPicker.invoke()
            TYPE_OPTIONAL_FILE -> onOptionalFile.invoke()
            else -> return
        }
    }

    private fun setEnableButton() {
        viewModel.setEnableButton(shouldEnableButton())
    }

    private fun shouldEnableButton(): Boolean = (
        selectedCvUri == null ||
            selectedKtpUri == null ||
            selectedIjazahUri == null ||
            selectedWorkExperienceUri == null ||
            selectedPortfolioUri == null
        ).not()

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uploadCertificateViewState.collect { viewState ->
                    handleUploadCertificateViewState(viewState)
                }
            }
        }
    }

    private fun processUploadDocuments() {
        val cv = selectedCvUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        } ?: return
        val ktp = selectedKtpUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        } ?: return
        val ijazah = selectedIjazahUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        } ?: return
        val workExperience = selectedWorkExperienceUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        } ?: return
        val portfolio = selectedPortfolioUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        } ?: return
        val optionalFile = selectedOptionalUri?.let { uri ->
            FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        }
        viewModel.uploadDocuments(
            cv = cv,
            ktp = ktp,
            ijazah = ijazah,
            workExperience = workExperience,
            portfolio = portfolio,
            optionalFile = optionalFile
        )
    }

    private fun checkCameraPermission(action: () -> Unit) {
        PermissionX.init(this).camera { action.invoke() }
    }

    private fun checkStoragePermission(action: () -> Unit) {
        PermissionX.init(this).storage { action.invoke() }
    }

    private fun handleUploadCertificateViewState(uiState: UiState<String>) {
        showLoading(uiState is UiState.Loading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> {
                binding.root.showSnackBar(uiState.data)
                finish()
            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
        pbLoading.isVisible = show
        btnUpload.isEnabled = !show
        btnPickCv.isEnabled = !show
        btnPickKtp.isEnabled = !show
        btnPickIjazah.isEnabled = !show
        btnPickWorkExperience.isEnabled = !show
        btnPickPortfolio.isEnabled = !show
        btnPickOptionalFile.isEnabled = !show
        btnUpload.isVisible = !show
    }

    private fun initListener() = with(binding) {
        btnPickCv.setOnClickListener {
            showFilePickerDialog(TYPE_CV) { uri ->
                selectedCvUri = uri
                cameraResultLauncher.launch(selectedCvUri)
            }
        }

        btnPickKtp.setOnClickListener {
            showFilePickerDialog(TYPE_KTP) { uri ->
                selectedKtpUri = uri
                cameraResultLauncher.launch(selectedKtpUri)
            }
        }

        btnPickIjazah.setOnClickListener {
            showFilePickerDialog(TYPE_IJAZAH) { uri ->
                selectedIjazahUri = uri
                cameraResultLauncher.launch(selectedIjazahUri)
            }
        }

        btnPickWorkExperience.setOnClickListener {
            showFilePickerDialog(TYPE_WORK_EXPERIENCE) { uri ->
                selectedWorkExperienceUri = uri
                cameraResultLauncher.launch(selectedWorkExperienceUri)
            }
        }

        btnPickPortfolio.setOnClickListener {
            showFilePickerDialog(TYPE_PORTFOLIO) { uri ->
                selectedPortfolioUri = uri
                cameraResultLauncher.launch(selectedPortfolioUri)
            }
        }

        btnPickOptionalFile.setOnClickListener {
            showFilePickerDialog(TYPE_OPTIONAL_FILE) { uri ->
                selectedOptionalUri = uri
                cameraResultLauncher.launch(selectedOptionalUri)
            }
        }

        btnUpload.setOnClickListener {
            processUploadDocuments()
        }
    }

    private fun showFilePickerDialog(pickerType: Int, onCamera: (Uri) -> Unit) {
        showImagePickerSourceDialog(
            onCamera = {
                checkCameraPermission {
                    pickFileType = pickerType
                    onCamera.invoke(getTempFileUri(format = FilePickerHelper.EXTENSION_PNG))
                }
            },
            onFile = {
                checkStoragePermission {
                    pickFileType = pickerType
                    pickerResultLauncher.launch(FilePickerHelper.getSingleFilePickIntent())
                }
            }
        )
    }

    companion object {
        private const val TYPE_CV = 0
        private const val TYPE_KTP = 1
        private const val TYPE_IJAZAH = 2
        private const val TYPE_WORK_EXPERIENCE = 3
        private const val TYPE_PORTFOLIO = 4
        private const val TYPE_OPTIONAL_FILE = 5
        fun start(context: Context) {
            context.openActivity(TrainingUploadCertificationActivity::class.java)
        }
    }
}
