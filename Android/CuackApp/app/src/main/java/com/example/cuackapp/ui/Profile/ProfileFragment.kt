package com.example.cuackapp.ui.Profile

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cuackapp.R
import com.example.cuackapp.data.datastore.SettingsManager
import com.example.cuackapp.databinding.FragmentProfileBinding
import com.example.cuackapp.model.userModel.ProfilePicToJson
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.System.load
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var settingsManager: SettingsManager
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentuser: User

    // 1. Nuevo lanzador para guardar a resolución completa
    private var latestTmpUri: Uri? = null

    private val profileVM : ProfileViewModel by viewModels()


    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            latestTmpUri?.let { uri ->
                Glide.with(this).load(uri).into(binding.ivProfilePicture)
                binding.ivProfilePicture.setImageURI(uri)

                currentuser.profilePicture = uri.toString()
                Log.i("ProfileFragment", "URI: $uri")

                Log.i("ProfileFragment", "URI: ${currentuser.profilePicture}")

                currentuser.profilePicture = uri.toString()
                val updateprofile = ProfilePicToJson(currentuser.profilePicture)

                profileVM.updateToServer(currentuser, updateprofile)



            }
        }
    }



    // 2. Lanzador para la Galería (Photo Picker moderno)
    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            // El URI es la dirección de la imagen en el dispositivo
            binding.ivProfilePicture.setImageURI(it)
            currentuser.profilePicture = it.toString()
            val updateprofile = ProfilePicToJson(currentuser.profilePicture)

            profileVM.updateToServer(currentuser, updateprofile)
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
    private fun createImageUri(): Uri? {
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "perfil_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // En Android 10+ se usa el MediaStore para guardarlo en la carpeta Pictures
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CuackApp")
            }
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupLanguageSpinner()

    }

    private fun initUI() {
        currentuser = obtenerUsuario()
        Log.i("ProfileFragment", "URI: ${currentuser.profilePicture}")
        binding.tvUsername.text = currentuser.username
        binding.tvRole.text = currentuser.type.name

        Glide.with(this).load(currentuser.profilePicture).into(binding.ivProfilePicture)


        initListeners()
    }

    private fun initListeners() {
        // switch de modo oscuro
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


     //Muestra un diálogo al usuario para elegir entre Cámara o Galería

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
                // Generamos el destino del archivo
                latestTmpUri = createImageUri()
                takePictureLauncher.launch(latestTmpUri)
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun setupLanguageSpinner() {
        val languages = listOf("English", "Español", "Euskara")
        val languageCodes = listOf("en", "es", "eu")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguage.adapter = adapter

        // 1. MARCAR IDIOMA ACTUAL: Evita que el Spinner siempre vuelva a "English" al abrir el fragment
        val currentCode = getCurrentLanguageCode()
        val initialPosition = languageCodes.indexOf(currentCode)
        if (initialPosition >= 0) {
            binding.spinnerLanguage.setSelection(initialPosition)
        }

        // 2. ESCUCHAR CAMBIOS: Solo cambia si el usuario elige algo distinto a lo que ya hay
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCode = languageCodes[position]
                if (selectedCode != getCurrentLanguageCode()) {
                    changeLanguage(selectedCode)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Función auxiliar para saber qué idioma tiene la app ahora mismo
    private fun getCurrentLanguageCode(): String {
        return AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"
    }
    private fun changeLanguage(langCode: String) {
        val appLocale: androidx.core.os.LocaleListCompat =
            androidx.core.os.LocaleListCompat.forLanguageTags(langCode)

        AppCompatDelegate.setApplicationLocales(appLocale)
    }


    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        val user = activity.currentUser
        return user
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


