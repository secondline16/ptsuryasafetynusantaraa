package com.ssn.app.ui.profile.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssn.app.data.api.request.EditProfileRequest
import com.ssn.app.databinding.ActivityEditProfileBinding
import com.ssn.app.extension.showSnackBar
import com.ssn.app.model.User
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initObserve()
        bindData(viewModel.getProfile())
        initListener()
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initListener() = with(binding) {
        btnSave.setOnClickListener {
            processEditProfile()
        }
    }

    private fun bindData(user: User) = with(binding) {
        edtAddress.setText(user.address)
        edtFullName.setText(user.fullName)
        edtPhoneNumber.setText(user.phone)
    }

    private fun processEditProfile() = with(binding) {
        val username = viewModel.getProfile().username
        val fullName = edtFullName.text.toString().trim()
        val address = edtAddress.text.toString().trim()
        val phoneNumber = edtPhoneNumber.text.toString().trim()
        val editProfileRequest = EditProfileRequest(
            username = username,
            name = fullName,
            phoneNumber = phoneNumber,
            address = address
        )
        viewModel.editProfile(editProfileRequest)
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        btnSave.isVisible = !show
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.editProfileViewState.collect { viewState ->
                    handleEditProfileViewState(viewState)
                }
            }
        }
    }

    private fun handleEditProfileViewState(uiState: UiState<String>) {
        showLoading(uiState is UiState.Loading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> binding.root.showSnackBar(uiState.e.message.orEmpty())
            is UiState.Success -> {
                setResult(RESULT_CODE_SUCCESS)
                finish()
            }
        }
    }

    companion object {
        const val RESULT_CODE_SUCCESS = 892
        fun getIntent(context: Context): Intent = Intent(context, EditProfileActivity::class.java)
    }
}
