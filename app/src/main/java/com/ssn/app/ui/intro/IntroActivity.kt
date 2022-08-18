package com.ssn.app.ui.intro

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssn.app.databinding.ActivityIntroBinding
import com.ssn.app.extension.openActivity
import com.ssn.app.ui.auth.login.LoginActivity
import com.ssn.app.ui.auth.register.RegisterActivity

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            navigateToLogin()
        }

        btnRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        RegisterActivity.start(this)
    }

    private fun navigateToLogin() {
        LoginActivity.start(this)
    }

    companion object {
        fun start(context: Context, clearTask: Boolean = false) {
            context.openActivity(IntroActivity::class.java, clearTask = clearTask)
        }
    }
}
