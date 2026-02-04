package com.example.cuackapp.ui.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentHomeDetailBinding
import com.example.cuackapp.databinding.FragmentMeetingsDetailBinding
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.exp

@AndroidEntryPoint
class HomeDetailFragment : BottomSheetDialogFragment() {

    // POP-UP DE LA INFO DEL HORARIO

    //VARIABLES A NIVEL DE PAGINA
    private var _binding : FragmentHomeDetailBinding? = null
    val binding get() = _binding!!
    private lateinit var currentUser: User
    private lateinit var selectedModule: Schedule


    // FUNCIONES
    //gestiona el comportamiento del pop-up
    override fun onStart() {
        super.onStart()
        val bottomSheet = binding.root.parent as? View
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // comienzo de flujo
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recupera la informacion que recibe de argumentos o de su actividad padre
        initData()
        // inicializa la interfaz visual dinamica
        initUI()


    }

    private fun initUI() {
        val horasMap = listOf("08:00", "09:00", "10:00", "11:30", "12:30", "13:30")
        binding.tvScheduleModule.text = selectedModule.module
        binding.tvDayOfWeek.text = selectedModule.day
        binding.tvHour.text = horasMap.get(selectedModule.hour - 1)
        binding.tvTeacher.text = if (currentUser.type.id == 4){selectedModule.extra}else{arguments?.getString(ARG_CLASS) ?: "Aula B101"}
        binding.tvAula.text = if (currentUser.type.id == 4){arguments?.getString(ARG_CLASS) ?: "Aula B101"}else{""}
    }

    private fun initData() {
        currentUser = obtenerUsuario()
        val module = arguments?.getString(ARG_MODULE) ?: ""
        val weekday = arguments?.getString(ARG_WEEKDAY) ?: ""
        val hour = arguments?.getInt(ARG_HOUR) ?: 0
        val extra = arguments?.getString(ARG_EXTRA) ?: ""

        when(currentUser.type.id){
            4 -> //alumno
                selectedModule = Schedule(extra, module, weekday, hour)
            3 ->//teacher
                selectedModule = Schedule("", module, weekday, hour)

        }

    }

    // COMPANION OBJECT
    //recibe datos por args y crea la instancia del objeto con esos datos
    companion object{
        private const val ARG_MODULE = "arg_module"
        private const val ARG_WEEKDAY = "arg_weekday"
        private const val ARG_HOUR = "arg_hour"
        private const val ARG_EXTRA = "arg_extra"
        private const val ARG_CLASS = "arg_class"

        fun newInstance(selectedModule: Schedule): HomeDetailFragment{
            val fragment = HomeDetailFragment()
            val args = Bundle()
            args.putString(ARG_MODULE, selectedModule.module)
            args.putString(ARG_WEEKDAY, selectedModule.day)
            args.putInt(ARG_HOUR, selectedModule.hour)
            args.putString(ARG_EXTRA, selectedModule.extra)
            args.putString(ARG_CLASS, "Aula B101")

            fragment.arguments = args

            return fragment
        }
    }


    // FUNCIONES EXTRA
    // llama a la actividad padre y obtiene el usuario actual
    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        val user = activity.currentUser

        return user
    }

    // limpieza de views
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}