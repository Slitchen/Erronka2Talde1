package com.example.cuackapp.ui.NavigationHUD

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cuackapp.R
import com.example.cuackapp.data.datastore.SettingsManager
import com.example.cuackapp.databinding.ActivityNavigationBinding
import com.example.cuackapp.model.UserModel.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsManager: SettingsManager

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var navController: NavController

    // Declaramos como lateinit para inicializarla en el onCreate
    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Recuperar el usuario ANTES de cualquier otra cosa
        val userJson = intent.getStringExtra("USER_JSON")
        if (userJson != null) {
            currentUser = Gson().fromJson(userJson, User::class.java)
        } else {
            // Opcional: manejar qué pasa si no llega el JSON (volver al Login, por ejemplo)
        }

        // 2. Aplicar el tema (Modo Oscuro)
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
    }
}