package com.example.cuackapp.ui.Login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cuackapp.R
import com.example.cuackapp.data.datastore.SettingsManager
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.example.cuackapp.databinding.LoginPageBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class LoginPage : AppCompatActivity() {

    @Inject
    lateinit var settingsManager: SettingsManager //gestiona rememberme
    private lateinit var binding: LoginPageBinding
    private val loginVM: LoginViewModel by viewModels() //ViewModel de donde recibira datos

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

        loadRememberMe()//mira si hay un usuario registrado de la ultima vez y lo setea
        initLoadingAnimation()// animacion inicial
        setupObservers()// activa un obervador que espera cambios en el viewmodel
        initListeners()//boton de login


    }

    private fun loadRememberMe() {
        //scope para no parar el hilo principal
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //collect de los datos ya que son Flow, no los pilla sinmas
                launch {
                    settingsManager.usernameFlow.collect { username ->
                        if (username.isNotEmpty()) {
                            binding.etUsername.setText(username)
                        }
                    }
                }
                launch {
                    settingsManager.passwordFlow.collect { password ->
                        if (password.isNotEmpty()) {
                            binding.etPassword.setText(password)
                        }
                    }
                }
            }
        }
    }


    private fun initLoadingAnimation() {
        val vueltas = 5
        val duracionGiro = 600L
        val pausa = 400L

        for (i in 0 until vueltas) {
            val animator = ObjectAnimator.ofFloat(binding.animLogo, View.ROTATION, 0f, 360f)
            animator.duration = duracionGiro
            animator.interpolator = DecelerateInterpolator()

            animator.startDelay = i * (duracionGiro + pausa)

            animator.start()
            if (i == vueltas - 1) {
                animator.doOnEnd {
                    binding.startCard.visibility = GONE
                }
            }
        }
    }


    fun initListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            //validación de campos
            if (username.isEmpty()) {
                binding.etUsername.error = "Username is required"
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
            } else {
                //check de que existe ese user
                loginVM.getLogin(username, password)
            }
        }
    }


    private fun setupObservers() {
        //lo mismo, scope para no bloquear el hilo principal mientras esperamos respuesta
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { //repite hasta recibir un cambio, collect de user
                loginVM.user.collect { user ->
                    if (user != null) {
                        //seteamos en el rememberme
                        settingsManager.setUserAndPassword(user.username, user.password)
                        // Lo convertimos a texto
                        val userJson = Gson().toJson(user)
                        //animacion de transicion
                        initLoginAnimation(userJson)
                    }
                }
            }
        }
    }


     fun initLoginAnimation(userJson: String) {
        val vueltas = 3
        val duracionGiro = 600L
        val pausa = 400L
        binding.btnLogin.visibility = GONE
        binding.startCard.visibility = VISIBLE


        for (i in 0 until vueltas) {
            val animator =
                ObjectAnimator.ofFloat(binding.animLogo, View.ROTATION, 0f, 360f)
            animator.duration = duracionGiro
            animator.interpolator = DecelerateInterpolator()
            animator.startDelay = i * (duracionGiro + pausa)

            animator.start()
            if (i == vueltas - 1) {
                animator.doOnEnd { //cuando termine, navega con intent a otra actividad
                    //navigation es la actividad con el container de fragments, donde navegaremos en el transcurso de la app
                    val intent =
                        Intent(this@LoginPage, NavigationActivity::class.java)
                    //pasamos el objeto user en un json a la otra actividad
                    intent.putExtra("USER_JSON", userJson)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}