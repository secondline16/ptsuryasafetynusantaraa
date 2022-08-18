package com.ssn.app.ui.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.permissionx.guolindev.PermissionX
import com.ssn.app.R
import com.ssn.app.common.PreviewImageDialog
import com.ssn.app.databinding.FragmentProfileBinding
import com.ssn.app.extension.orDash
import com.ssn.app.extension.showSnackBar
import com.ssn.app.helper.Helper.camera
import com.ssn.app.helper.viewBinding
import com.ssn.app.model.User
import com.ssn.app.ui.intro.IntroActivity
import com.ssn.app.ui.profile.edit.EditProfileActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private var avatarUrl: String? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result == null) return@registerForActivityResult
        if (result.isSuccessful) {
            result.getUriFilePath(requireContext())?.let { path ->
                viewModel.updateAvatar(File(path))
            }
        }
    }

    private val activityResultContracts = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult? ->
        if (activityResult == null) return@registerForActivityResult
        if (activityResult.resultCode == EditProfileActivity.RESULT_CODE_SUCCESS) {
            viewModel.getProfile(fetchNetwork = false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initListener()
    }

    private fun initListener() = with(binding) {
        btnLogout.setOnClickListener {
            viewModel.logout()
        }

        btnEdit.setOnClickListener {
            navigateToEditProfile()
        }

        ivProfile.setOnClickListener {
            showImageProfileDialog(
                onChangeImage = { requestCameraPermission { pickImageCropper() } },
                onPreviewImage = { showImagePreviewDialog() }
            )
        }
    }

    private fun showImagePreviewDialog() {
        avatarUrl?.let { url ->
            PreviewImageDialog.newInstance(url).also { dialog ->
                dialog.show(childFragmentManager, PreviewImageDialog.TAG)
            }
        }
    }

    private fun showImageProfileDialog(
        onPreviewImage: () -> Unit,
        onChangeImage: () -> Unit
    ) {
        val items = arrayOf("Lihat Gambar", "Ubah Gambar")
        MaterialAlertDialogBuilder(requireContext()).apply {
            setItems(items) { dialog, index ->
                when (index) {
                    0 -> onPreviewImage.invoke()
                    1 -> onChangeImage.invoke()
                }
                dialog.dismiss()
            }
        }.show()
    }

    private fun requestCameraPermission(action: () -> Unit) {
        PermissionX.init(this).camera { action.invoke() }
    }

    private fun pickImageCropper() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setImageSource(includeCamera = true, includeGallery = true)
                setAspectRatio(1, 1)
                setCropShape(CropImageView.CropShape.OVAL)
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )
    }

    private fun navigateToEditProfile() {
        context?.let { context ->
            activityResultContracts.launch(EditProfileActivity.getIntent(context))
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.profileViewState.collect { viewState ->
                    handleProfileViewState(viewState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.logoutViewState.collect { viewState ->
                    handleLogoutViewState(viewState)
                }
            }
        }
    }

    private fun handleLogoutViewState(uiState: UiState<Any?>) {
        when (uiState) {
            is UiState.Loading -> Unit // TODO show logout loading
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> navigateToIntro()
        }
    }

    private fun navigateToIntro() {
        IntroActivity.start(requireContext(), clearTask = true)
    }

    private fun handleProfileViewState(uiState: UiState<User>) {
        when (uiState) {
            is UiState.Loading -> uiState.data?.let(::bindData)
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> bindData(uiState.data)
        }
    }

    private fun bindData(user: User) = with(binding) {
        avatarUrl = user.photo
        tvUsername.text = user.username.orDash()
        tvEmail.text = user.email.orDash()
        tvAddress.text = user.address.orDash()
        tvFullName.text = user.fullName.orDash()
        tvPhoneNumber.text = user.phone.orDash()
        ivProfile.load(user.photo)
    }
}
