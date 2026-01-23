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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentHomeBinding
import com.example.cuackapp.model.UserModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var currentUser : User

    private val schedulesVM : HomeViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. Aquí SÍ es seguro obtener el usuario, porque la Activity ya existe para el Fragment
        currentUser = obtenerUsuario()

        setupObservers()
        initListeners()

        schedulesVM.getSchedules(currentUser)
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
                        Log.i( "HomeFragment", "La lista de schedules está vacía.")
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            )

        }
    }



    private fun renderGrid(modules: List<Schedule>) {
        val grid = binding.scheduleGrid
        grid.removeAllViews()

        var index = 0 // Empezamos en 0 para aprovechar toda la lista

        for (r in 0 until 7) { // 7 filas (0 a 6)
            for (c in 0 until 6) { // 6 columnas (0 a 5)

                if (r == 0 || c == 0) {
                    // Es un encabezado (Lunes, Martes... o las Horas)
                    grid.addView(createCell("", r, c))
                } else {
                    // Es una celda de contenido
                    if (index < modules.size) {
                        val textoAMostrar = modules[index].module ?: ""
                        grid.addView(createCell(textoAMostrar, r, c))
                        index++
                    } else {
                        // Si nos quedamos sin datos en la lista, ponemos una celda vacía
                        // para que el diseño del Grid no se rompa
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
        textView.gravity = Gravity.CENTER

        //depende si es fila o columna 0 le pone un borde u otro
        if (row == 0 || col == 0) {
            textView.setBackgroundResource(R.drawable.cell_border_title) // Borde especial para encabezados
            textView.setTypeface(null, android.graphics.Typeface.BOLD)
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
    private fun obtenerUsuario(): User {
        // Ahora requireActivity() funcionará perfectamente
        val activity = requireActivity() as NavigationActivity
        return activity.currentUser
    }


}

