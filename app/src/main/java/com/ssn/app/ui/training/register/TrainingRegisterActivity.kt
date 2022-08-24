package com.ssn.app.ui.training.register

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
import com.ssn.app.databinding.ActivityTrainingRegisterBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.setDocumentIcon
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.FilePickerHelper
import com.ssn.app.helper.FilePickerHelper.EXTENSION_PNG
import com.ssn.app.helper.FilePickerHelper.doIfImage
import com.ssn.app.helper.FilePickerHelper.getTempFileUri
import com.ssn.app.helper.Helper.camera
import com.ssn.app.helper.Helper.showImagePickerSourceDialog
import com.ssn.app.helper.Helper.storage
import com.ssn.app.ui.training_following.detail.TrainingFollowingDetailActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch
import java.io.File

class TrainingRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingRegisterBinding
    private val viewModel: TrainingRegisterViewModel by viewModels()
    private var id: Int = 0
    private var selectedFileUri: Uri? = null

    private val pickerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult? ->
        if (activityResult == null) return@registerForActivityResult
        selectedFileUri = activityResult.data?.data ?: return@registerForActivityResult
        setImagePreview()
        enableRegisterButton()
    }

    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            setImagePreview()
            enableRegisterButton()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        processIntentExtras()
        initObserve()
        initListener()
    }

    private fun setImagePreview() {
        selectedFileUri.doIfImage(
            context = this,
            action = { file ->
                binding.icFilePreview.apply { isVisible = true; load(file) }
            },
            onNotImage = {
                binding.icFilePreview.apply { isVisible = true; setDocumentIcon() }
            }
        )
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.registerTrainingViewState.collect { viewState ->
                    handleRegisterTrainingViewState(viewState)
                }
            }
        }
    }

    private fun enableRegisterButton(enable: Boolean = true) {
        binding.btnRegisterTraining.isEnabled = enable
    }

    private fun handleRegisterTrainingViewState(uiState: UiState<String>) {
        showLoading(uiState is UiState.Loading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> {
                navigateToDetailFollowingTraining()
                finish()
            }
        }
    }

    private fun navigateToDetailFollowingTraining() {
        TrainingFollowingDetailActivity.start(this, id)
    }

    private fun showLoading(show: Boolean) = with(binding) {
        btnRegisterTraining.isVisible = !show
        btnPickFile.isEnabled = !show
        pbLoading.isVisible = show
    }

    private fun initListener() = with(binding) {
        btnPickFile.setOnClickListener {
            showImagePickerSourceDialog(
                onCamera = {
                    checkCameraPermission {
                        selectedFileUri = getTempFileUri(format = EXTENSION_PNG)
                        cameraResultLauncher.launch(selectedFileUri)
                    }
                },
                onFile = {
                    checkStoragePermission {
                        pickerResultLauncher.launch(FilePickerHelper.getSingleFilePickIntent())
                    }
                }
            )
        }
        btnRegisterTraining.setOnClickListener {
            registerTraining()
        }
    }

    private fun registerTraining() {
        val uri = selectedFileUri ?: return
        val file = FilePickerHelper.getFilePathFromUri(this, uri).run(::File)
        viewModel.registerTraining(id, file)
    }

    private fun checkCameraPermission(action: () -> Unit) {
        PermissionX.init(this).camera { action.invoke() }
    }

    private fun checkStoragePermission(action: () -> Unit) {
        PermissionX.init(this).storage { action.invoke() }
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.apply {
            title = getString(R.string.label_form_daftar_pelatihan)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun processIntentExtras() {
        id = intent.getIntExtra(EXTRA_ID, 0)
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: Int) {
            context.openActivity(TrainingRegisterActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }
    }
}
