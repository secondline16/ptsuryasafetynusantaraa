package com.ssn.app.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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
        initNavigation()
    }

    private fun initNavigation() = with(binding) {
        nvMenu.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.tbBase.title = destination.label
            when (destination.id) {
                R.id.aboutFragment,
                R.id.trainingIndexFragment,
                R.id.trainingFollowingIndexFragment,
                R.id.profileFragment,
                R.id.jobVacancyIndexFragment
                -> {
                    toolbar.tbBase.apply {
                        navigationIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_hamburger)
                        setNavigationOnClickListener { showDrawer() }
                    }
                }
            }
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

    private fun showDrawer() = with(binding.drlParent) {
        if (isDrawerOpen(GravityCompat.START)) {
            closeDrawer(GravityCompat.START)
        } else {
            openDrawer(GravityCompat.START)
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
