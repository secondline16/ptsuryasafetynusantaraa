package com.ssn.app.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ssn.app.R
import com.ssn.app.databinding.ActivityMainBinding
import com.ssn.app.extension.openActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvNavHost) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initNavigation()
    }

    private fun initToolbar() {
        binding.toolbar.tbBase.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initNavigation() = with(binding) {
        nvMenu.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.tbBase.title = destination.label
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        NavigationUI.navigateUp(navController, binding.drlParent)

    override fun onBackPressed() = with(binding) {
        if (drlParent.isDrawerOpen(GravityCompat.START)) {
            drlParent.closeDrawer(GravityCompat.START)
        } else {
            if (!navController.navigateUp()) super.onBackPressed()
        }
    }

    companion object {
        fun start(context: Context, clearTask: Boolean = false) {
            context.openActivity(
                destination = MainActivity::class.java,
                clearTask = clearTask
            )
        }
    }
}
