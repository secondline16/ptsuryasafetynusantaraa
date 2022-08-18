package com.ssn.app.ui.auth.register

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssn.app.data.api.request.RegisterRequest
import com.ssn.app.databinding.ActivityRegisterBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.showSnackBar
import com.ssn.app.ui.auth.login.LoginActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initObserve()
        initListener()
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.registerViewState.collect { viewState ->
                    handleRegisterViewState(viewState)
                }
            }
        }
    }

    private fun handleRegisterViewState(uiState: UiState<String>) = with(binding) {
        showLoading(uiState is UiState.Loading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> {
                root.showSnackBar(uiState.e.message.orEmpty())
            }
            is UiState.Success -> {
                root.showSnackBar(uiState.data)
                navigateToLogin()
                finish()
            }
        }
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        btnRegister.isVisible = !show
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initListener() = with(binding) {
        btnRegister.setOnClickListener { // TODO handle register process
            registerProcess()
        }
    }

    private fun registerProcess() = with(binding) {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val rePassword = edtRePassword.text.toString().trim()
        val fullName = edtFullName.text.toString().trim()
        val address = edtAddress.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val phoneNumber = edtPhoneNumber.text.toString().trim()

        val registerRequest = RegisterRequest(
            username = username,
            password = password,
            passwordConfirmation = rePassword,
            name = fullName,
            email = email,
            phoneNumber = phoneNumber,
            address = address
        )
        viewModel.register(registerRequest)
    }

    private fun navigateToLogin() {
        LoginActivity.start(this)
    }

    companion object {
        fun start(context: Context) {
            context.openActivity(RegisterActivity::class.java)
        }
    }
}
