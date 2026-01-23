package com.example.cuackapp.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cuackapp.R
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.example.cuackapp.databinding.LoginPageBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginPage : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding

    private val loginVM: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
        initListeners()


    }


    fun initListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty()) {
                binding.etUsername.error = "Username is required"
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
            } else {
                // Solo llamamos a la función, no navegamos aquí
                loginVM.getLogin(username, password)
            }
        }
    }

    // Crea esta función y llámala en el onCreate
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.user.collect { user ->
                    if (user != null) {
                        val userJson = Gson().toJson(user) // Lo convertimos a texto

                        val intent = Intent(this@LoginPage, NavigationActivity::class.java)
                        intent.putExtra("USER_JSON", userJson)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
