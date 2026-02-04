package com.example.cuackapp.ui.Users

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentUserDetailBinding
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.model.userModel.UserType
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.ui.Home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class UserDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private val schedulesVM : HomeViewModel by viewModels ()

    override fun onStart() {
        super.onStart()
        val bottomSheet = binding.root.parent as? View
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperamos el nombre y los datos de los argumentos de forma segura

        val id = arguments?.getString(ARG_ID)
        val username = arguments?.getString(ARG_USERNAME)
        val role = arguments?.getString(ARG_ROLE)
        val image = arguments?.getString(ARG_IMAGE)
        val ciclo = arguments?.getString(ARG_CICLO)


        val selectedUser = User(
            id = id?.toInt() ?: 0,
            username = username ?: "",
            email = "", password = "", nombre = "", apellidos = "", dni = "",
            direccion = "", telefono1 = "", telefono2 = "",
            profilePicture = image ?: "",
            type = UserType(
                id = when (role?.lowercase()) {
                    "profesor" -> 3
                    "alumno" -> 4
                    else -> 0
                },
                name = role ?: "",
                nameEu = ""
            ),
            createdAt = "", updatedAt = "",
            cicloId = 0,
            ciclo = ciclo?: "",
            curso = ""
        )
        setupObservers()
        initUi(selectedUser)

        when(role) {
            "profesor" -> schedulesVM.getSchedules(selectedUser)
            "alumno" -> schedulesVM.getSchedules(selectedUser)
            else -> defaultGridMessage()
        }

    }

    private fun initUi(selectedUser: User) {
        binding.tvDetailUsername.text = selectedUser.username
        binding.tvDetailRole.text = selectedUser.type.name
        binding.tvDetailCiclo.text = selectedUser.ciclo

        Glide.with(this)
            .load(selectedUser.profilePicture)
            .circleCrop()
            .into(binding.ivDetailProfilePicture)

    }

    //el companion object sirve para crear instancias del fragment con argumentos
    companion object {
        //instanciamos lor agrumentos que necesitamos para mostrar el detalle
        private const val ARG_ID = "arg_id"
        private const val ARG_USERNAME = "arg_username"
        private const val ARG_ROLE = "arg_role"
        private const val ARG_IMAGE = "arg_image"
        private const val ARG_CICLO = "arg_ciclo"






        // le pasamos el user en cada click para rellenar los argumentos dinamicamente
        fun newInstance(user: User): UserDetailFragment {
            val fragment = UserDetailFragment()
            val args = Bundle()
            args.putString(ARG_ID, user.id.toString())
            args.putString(ARG_USERNAME, user.username)
            args.putString(ARG_ROLE, user.type.name)
            args.putString(ARG_IMAGE, user.profilePicture)
            args.putString(ARG_CICLO, user.ciclo)

            fragment.arguments = args


            return fragment
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Aquí "escuchamos" los cambios de la lista
                schedulesVM.schedules.collect { listaDeSchedules ->
                    Log.i("HomeFragment", "Schedules recibidos: $listaDeSchedules")
                    if (listaDeSchedules.isNotEmpty()) {
                        // 3. Cuando lleguen los datos, pintamos el Grid
                        renderGrid(listaDeSchedules)

                    }else{

                    }
                }
            }
        }
    }

    private fun renderGrid(modules: List<Schedule>) {
        val grid = binding.scheduleGrid
        grid.removeAllViews()
        val weekMap = mapOf(1 to "LUNES",2 to "MARTES",3 to "MIERCOLES",4 to "JUEVES",5 to "VIERNES")

        var index = 0 // Empezamos en 0 para aprovechar toda la lista

        for (c in 0 until 6) { // 6 columnas (0 a 5)
            for (r in 0 until 7) { // 7 filas (0 a 6)

                if (r == 0 || c == 0) {
                    // Es un encabezado (Lunes, Martes... o las Horas)
                    grid.addView(createCell("", r, c))
                } else {
                    // Buscamos si existe un módulo para este día (c) y hora (r)
                    val modulosEnEseHueco = modules.filter { it.day == weekMap[c] && it.hour == r }

                    if (modulosEnEseHueco.isNotEmpty()) {
                        // Si existe, lo pintamos
                        grid.addView(createCell(modulosEnEseHueco[0].module, r, c))
                    } else {
                        // Si no existe horario en esa celda, ponemos un vacío o "-"
                        grid.addView(createCell("-", r, c))
                    }
                }
            }
        }
    }
    fun createCell(text: String, row: Int, col: Int): TextView {
        //crea un tv
        val textView = TextView(context)
        textView.text = text
        textView.textSize = 12f
        textView.setTextColor(ContextCompat.getColorStateList(context, R.color.white))
        textView.gravity = Gravity.CENTER

        //depende si es fila o columna 0 le pone un borde u otro
        if (row == 0 || col == 0) {
            textView.setBackgroundResource(R.drawable.cell_border_title) // Borde especial para encabezados
            textView.setTypeface(null, Typeface.BOLD)
            if (row==0){
                when(col){
                    0 -> textView.text = ""
                    1 -> textView.text = "L"
                    2 -> textView.text = "M"
                    3 -> textView.text = "X"
                    4 -> textView.text = "J"
                    5 -> textView.text = "V"

                }
            } else if (col==0){
                when(row){
                    0 -> textView.text = ""
                    1 -> textView.text = "8:00 - 9:00"
                    2 -> textView.text = "9:00 - 10:00"
                    3 -> textView.text = "10:00 - 11:00"
                    4 -> textView.text = "11:30 - 12:30"
                    5 -> textView.text = "12:30 - 13:30"
                    6 -> textView.text = "13:30 - 14:30"
                }

            }
        } else{
            textView.setBackgroundResource(R.drawable.cell_border) // Un borde para que parezca tabla
        }


        // Definir posición en el Grid
        val params = GridLayout.LayoutParams()
        params.rowSpec = GridLayout.spec(row, 1f)    // Fila y peso (weight)
        params.columnSpec = GridLayout.spec(col, 1f) // Columna y peso
        params.width = 0
        params.height = GridLayout.LayoutParams.WRAP_CONTENT
        params.setMargins(2, 2, 2, 2)

        textView.layoutParams = params
        return textView
    }
    private fun defaultGridMessage() {
        val grid = binding.scheduleGrid
        grid.removeAllViews()

        // Configuramos el Grid para que tenga solo 1 columna y 1 fila para el mensaje
        grid.columnCount = 1
        grid.rowCount = 1

        val textView = TextView(context).apply {
            text = "⚠️ Horario no disponible"
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(requireContext(), R.color.slate_400))
            setPadding(0, 50, 0, 50)
            textSize = 16f
        }

        val params = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(0, 1f)
            columnSpec = GridLayout.spec(0, 1f)
            width = GridLayout.LayoutParams.MATCH_PARENT
            height = 300 // Altura fija para que se vea bien en el BottomSheet
        }

        textView.layoutParams = params
        grid.addView(textView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}