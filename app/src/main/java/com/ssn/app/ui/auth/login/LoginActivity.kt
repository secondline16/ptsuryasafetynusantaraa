package com.ssn.app.ui.auth.login

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssn.app.databinding.ActivityLoginBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.extension.showSnackBar
import com.ssn.app.ui.main.MainActivity
import com.ssn.app.vo.UiState
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initObserve()
        initListener()
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loginViewState.collect { viewState ->
                    handleLoginViewState(viewState)
                }
            }
        }
    }

    private fun handleLoginViewState(uiState: UiState<String>) = with(binding) {
        showLoading(uiState is UiState.Loading)
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Error -> {
                root.showSnackBar(uiState.e.message.orEmpty())
            }
            is UiState.Success -> {
                root.showSnackBar(uiState.data)
                navigateToMain()
            }
        }
    }

    private fun showLoading(show: Boolean = true) = with(binding) {
        pbLoading.isVisible = show
        btnLogin.isVisible = !show
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            processLogin()
        }
    }

    private fun processLogin() = with(binding) {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        viewModel.login(email, password)
    }

    private fun navigateToMain() {
        MainActivity.start(this, clearTask = true)
    }

    companion object {
        fun start(context: Context) {
            context.openActivity(LoginActivity::class.java)
        }
    }
}
