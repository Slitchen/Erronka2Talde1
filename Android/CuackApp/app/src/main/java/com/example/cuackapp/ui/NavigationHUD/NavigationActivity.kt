package com.example.cuackapp.ui.NavigationHUD

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cuackapp.R
import com.example.cuackapp.data.datastore.SettingsManager
import com.example.cuackapp.databinding.ActivityNavigationBinding
import com.example.cuackapp.model.userModel.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject
@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsManager: SettingsManager

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var navController: NavController

    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserFromIntent()
        applyDarkMode()

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()
    }
    private fun getUserFromIntent() {
        val userJson = intent.getStringExtra("USER_JSON")
        if (userJson != null) {
            currentUser = Gson().fromJson(userJson, User::class.java)
        } else {
        }
    }

    private fun applyDarkMode() {
        lifecycleScope.launchWhenCreated {
            val isDark = settingsManager.darkModeFlow.first()
            val targetMode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            if (AppCompatDelegate.getDefaultNightMode() != targetMode) {
                AppCompatDelegate.setDefaultNightMode(targetMode)
            }
        }
    }

    private fun initUI() {
        val navHost: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomBar.setupWithNavController(navController)

        initListener()
    }

    private fun initListener() {
        binding.bottomBar.setOnItemSelectedListener { item ->
            // limpia el historial de la pestaña antes de navegar
            val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(false) //evitar que vuelva al Profile
                .setPopUpTo(navController.graph.startDestinationId, inclusive = false, saveState = false)

            try {
                navController.navigate(item.itemId, null, builder.build())
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }
}