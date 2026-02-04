package com.example.cuackapp.ui.Meetings

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
import com.example.cuackapp.databinding.FragmentMeetingsBinding
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.Home.HomeViewModel
import com.example.cuackapp.ui.Meetings.DetailsDialog.CreateMeetingsDetailFragment
import com.example.cuackapp.ui.Meetings.DetailsDialog.MeetingsDetailFragment
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MeetingsFragment : Fragment() {

    private var _binding: FragmentMeetingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser: User
    private val meetingsVM: MeetingsViewModel by viewModels()
    private val schedulesVM : HomeViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = obtenerUsuario()
        setupObservers()
        initListeners()
        meetingsVM.getMeetings(currentUser)
        schedulesVM.getSchedules(currentUser)

    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Aquí "escuchamos" los cambios de la lista
                meetingsVM.meetings.collect { listaDeMeetings ->

                    if (listaDeMeetings.isNotEmpty()) {
                        // Cuando lleguen los datos, pintamos el Grid
                        schedulesVM.schedules.collect { listaDeSchedules ->
                            renderGrid(listaDeMeetings, listaDeSchedules)
                        }

                    } else {
                        defaultGridMessage()

                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSettings.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.homeFragment, inclusive = false)
                .build()

            findNavController().navigate(
                R.id.profileFragment, // Usamos el ID del destino directamente
                null,
                navOptions
            )
        }
        binding.btnAddMeeting.setOnClickListener {
            showMeetingCreationPopUp()
        }

    }


    private fun renderGrid(meetings: List<Meeting>, schedules: List<Schedule> ) {
        val grid = binding.scheduleGrid
        grid.removeAllViews()

        grid.columnCount = 6
        grid.rowCount = 7

        val diasMap = listOf("2026-01-26", "2026-01-27", "2026-01-28", "2026-01-29", "2026-01-30")
        val horasMap = listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00")
        val weekMap = mapOf(1 to "LUNES",2 to "MARTES",3 to "MIERCOLES",4 to "JUEVES",5 to "VIERNES")

        for (r in 0 until 7) {
            for (c in 0 until 6) {
                if (r == 0 || c == 0) {
                    // Header cells don't need a meeting object
                    grid.addView(createCell("", r, c, null))
                } else {
                    val diaActual = diasMap[c - 1]
                    val horaActual = horasMap[r - 1]
                    val targetDateTime = "${diaActual}T${horaActual}"

                    val meetingEnCelda = meetings.find { it.scheduled_date.startsWith(targetDateTime) }

                    if (meetingEnCelda != null) {
                        if (currentUser.type.id == 3) {//profe
                            val claseConflicto =schedules.find { it.day == weekMap.get(c) && it.hour == r }
                            Log.i("MeetingsFragment", "Clase de conflicto: $claseConflicto")

                            if (claseConflicto != null && meetingEnCelda.state == "pendiente") {
                                Log.i("MeetingsFragment", "Reunión en conflicto detectada: ${meetingEnCelda.id}")
                                meetingEnCelda.state = "conflicto"
                                grid.addView(
                                    createCell(meetingEnCelda.state ?: "pendiente",r,c,meetingEnCelda)
                                )

                            }else{

                                grid.addView(createCell(meetingEnCelda.state ?: "pendiente", r, c, meetingEnCelda))

                            }
                        }else{//alumno
                            Log.i("MeetingsFragment", "Reunión en celda: ${meetingEnCelda.id}")
                            grid.addView(createCell(meetingEnCelda.state ?: "pendiente", r, c, meetingEnCelda))

                        }
                         } else {
                        grid.addView(createCell("-", r, c, null))
                    }
                }
            }
        }
    }

    fun createCell(text: String, row: Int, col: Int, meeting: Meeting?): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.textSize = 12f
        textView.setTextColor(ContextCompat.getColorStateList(context, R.color.white))
        textView.gravity = Gravity.CENTER
        if (row == 0 || col == 0) {
            textView.setBackgroundResource(R.drawable.cell_border_title)
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
        } else {
            textView.setBackgroundResource(R.drawable.cell_border)
            if (meeting != null) {
                textView.setOnClickListener {
                    showMeetingPopUp(meeting)
                }
                Log.i("MeetingsFragment", "Configurando celda para reunión con estado: ${meeting.id}")

                Log.i("MeetingsFragment", "Configurando celda para reunión con estado: ${meeting.state}")
                when(meeting.state){
                    "pendiente" -> {
                        textView.setBackgroundResource(R.drawable.cell_border_pendiente)

                    }
                    "aceptada" -> {
                        textView.setBackgroundResource(R.drawable.cell_border_confirmada)
                    }
                    "denegada" -> {
                        textView.setBackgroundResource(R.drawable.cell_border_cancelada)
                    }
                    "conflicto" -> {
                        textView.setBackgroundResource(R.drawable.cell_border_conflicto)
                    }
                }

            }
        }

        val params = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(row, 1f)
            columnSpec = GridLayout.spec(col, 1f)
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            setMargins(2, 2, 2, 2)
        }
        textView.layoutParams = params
        return textView
    }
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


    fun showMeetingPopUp(selectedMeeting: Meeting) {
        val detailSheet = MeetingsDetailFragment.newInstance(selectedMeeting)
        detailSheet.show(parentFragmentManager, "MeetingDetail")
    }
    fun showMeetingCreationPopUp() {
        val detailSheet = CreateMeetingsDetailFragment()
        detailSheet.show(parentFragmentManager, "CreateMeetingDetail")
    }
    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        val user = activity.currentUser // Acceso directo a la variable

        return user
    }

}


