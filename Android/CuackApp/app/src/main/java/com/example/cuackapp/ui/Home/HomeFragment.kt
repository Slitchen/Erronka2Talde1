package com.example.cuackapp.ui.Home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentHomeBinding
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    // PAGINA PRINCIPAL --> SE MUESTRA EL HORARIO DEL USUARIO LOGGUEADO

    //VARIABLES A NIVEL DE PAGINA
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser : User
    private val schedulesVM : HomeViewModel by viewModels ()



    // FUNCIONES
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }


    // comienzo de flujo
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener el usuario actual desde la actividad contenedora
        currentUser = obtenerUsuario()
        // inicializa la interfaz visual dinamica
        initUI()
        // inicializa y settea observers para cambios en los datos que recibimos
        setupObservers()
        // inicializa los botones
        initListeners()
        // Dependiendo del tipo de usuario, mostramos el horario o un mensaje por defecto
        if (currentUser.type.id == 1 || currentUser.type.id == 2 ){
            defaultGridMessage()
        } else {
            schedulesVM.getSchedules(currentUser)
        }

    }






    private fun initUI() {
        binding.tvWelcome.text =  "${binding.tvWelcome.text}, ${currentUser.username}"
    }

    // observer para la respuesta del viewmodel (que llama a la api)
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Aquí llamamos y "escuchamos" los cambios de la lista
                schedulesVM.schedules.collect { listaDeSchedules ->
                    Log.i("HomeFragment", "Schedules recibidos: $listaDeSchedules")
                    if (listaDeSchedules.isNotEmpty()) {
                        // Cuando lleguen los datos, pintamos el Grid
                        renderGrid(listaDeSchedules)
                    }else{
                        Log.i( "HomeFragment", "La lista de schedules está vacía.")
                    }
                }
            }
        }
    }
    private fun initListeners() {
        binding.btnSettings.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                // Al ir al profile, "sacamos" el home de la pila de historial
                // para que no se guarde el estado con el profile encima.
                .setPopUpTo(R.id.homeFragment, inclusive = false)
                .build()

            findNavController().navigate(
                R.id.profileFragment,
                null,
                navOptions
            )
        }
    }

    //funcion que pinta el grid
    private fun renderGrid(modules: List<Schedule>) {
        val grid = binding.scheduleGrid
        grid.removeAllViews()
        val weekMap = mapOf(1 to "LUNES",2 to "MARTES",3 to "MIERCOLES",4 to "JUEVES",5 to "VIERNES")

        for (c in 0 until 6) { // 6 columnas (0 a 5)
            for (r in 0 until 7) { // 7 filas (0 a 6)

                if (r == 0 || c == 0) {
                    // Es un encabezado (Lunes, Martes... o las Horas)
                    grid.addView(createCell("", r, c, modules[0]))
                } else {

                    // Buscamos si existe un módulo para este día (c) y hora (r)
                    val moduloEnEseHueco = modules.find { it.day == weekMap[c] && it.hour == r }
                    if (moduloEnEseHueco != null) {
                        // Si existe, lo pintamos
                        grid.addView(createCell(moduloEnEseHueco.module, r, c, moduloEnEseHueco))
                    } else {
                        // Si no existe horario en esa celda, ponemos un vacío o "-"
                        grid.addView(createCell("-", r, c, null))
                    }
                }
            }
        }
    }
    //funcion que crea una celda del grid que se ejecuta en bucle
    fun createCell(text: String, row: Int, col: Int, modulo: Schedule?): TextView {
        //crea un tv
        val textView = TextView(context)
        textView.text = text
        textView.textSize = 12f
        textView.setTextColor(ContextCompat.getColorStateList(context, R.color.white))
        textView.gravity = Gravity.CENTER

        //depende si es fila o columna 0 le pone un borde u otro
        if (row == 0 || col == 0) {
            textView.setBackgroundResource(R.drawable.cell_border_title) // Borde especial para encabezados
            textView.setTypeface(null, android.graphics.Typeface.BOLD)
            // gestion de encabezados
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
            if (modulo != null){
            textView.setOnClickListener {
                showModulePopUp(modulo)
            }

            }
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

   private fun showModulePopUp(selectedModule: Schedule) {
        val detailSheet = HomeDetailFragment.newInstance(selectedModule)
        detailSheet.show(parentFragmentManager, "HomeDetail")
    }

    //funcion que muestra un mensaje por defecto en el grid
    private fun defaultGridMessage() {
        val grid = binding.scheduleGrid
        grid.removeAllViews()
        Log.i("HomeFragment", "God or Admin - mostrando mensaje de horario no disponible")

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
    //funcion que obtiene el usuario actual desde la actividad contenedora
    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        return activity.currentUser
    }


}

