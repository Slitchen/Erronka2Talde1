package com.example.cuackapp.ui.Profile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cuackapp.data.datastore.SettingsManager
import com.example.cuackapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var settingsManager: SettingsManager
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // 1. Lanzador para la Cámara
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            binding.ivProfilePicture.setImageBitmap(it)
        }
    }

    // 2. Lanzador para la Galería (Photo Picker moderno)
    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            // El URI es la dirección de la imagen en el dispositivo
            binding.ivProfilePicture.setImageURI(it)
        }
    }

    // 3. Lanzador para pedir permiso de Cámara
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        // Lógica de Dark Mode
        viewLifecycleOwner.lifecycleScope.launch {
            val isDarkInitial = settingsManager.darkModeFlow.first()
            binding.switchDarkMode.setOnCheckedChangeListener(null)
            binding.switchDarkMode.isChecked = isDarkInitial

            binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                viewLifecycleOwner.lifecycleScope.launch {
                    settingsManager.setDarkMode(isChecked)
                    AppCompatDelegate.setDefaultNightMode(
                        if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

        // Botón de Volver
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botón de Editar Foto (Abre el diálogo de selección)
        binding.btnEditProfilePicture.setOnClickListener {
            showImageOptionsDialog()
        }
    }

    /**
     * Muestra un diálogo al usuario para elegir entre Cámara o Galería
     */
    private fun showImageOptionsDialog() {
        val options = arrayOf("Hacer foto con cámara", "Elegir de la galería", "Cancelar")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cambiar foto de perfil")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkPermissionAndLaunchCamera()
                1 -> pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun checkPermissionAndLaunchCamera() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                takePictureLauncher.launch(null)
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}