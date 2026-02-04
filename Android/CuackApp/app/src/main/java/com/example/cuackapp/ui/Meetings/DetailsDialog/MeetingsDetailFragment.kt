package com.example.cuackapp.ui.Meetings.DetailsDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentMeetingsDetailBinding
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.Meetings.MeetingsViewModel
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class MeetingsDetailFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentMeetingsDetailBinding? = null
    val binding get() = _binding!!

    private lateinit var selectedMeeting: Meeting

    private lateinit var currentUser: User
    private val meetingsVM: MeetingsViewModel by activityViewModels()


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
        _binding = FragmentMeetingsDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initUi()
        setupMap()

    }

    private fun initData() {
        currentUser = obtenerUsuario()
        //recogemos los argumentos
        val meetingId = arguments?.getInt(ARG_MEETING_ID) ?: -1
        val meetingProfeId = arguments?.getInt(ARG_PROFE_ID) ?: -1
        val meetingAlumnoId = arguments?.getInt(ARG_ALUMNO_ID) ?: -1
        val meetingState = arguments?.getString(ARG_STATE) ?: ""
        val meetingDate = arguments?.getString(ARG_DATE) ?: ""
        val meetingTitle = arguments?.getString(ARG_TITLE) ?: ""
        val meetingDescription = arguments?.getString(ARG_DESCRIPTION) ?: ""


        selectedMeeting = Meeting(
            id = meetingId,
            profesor_id = meetingProfeId,
            alum = meetingAlumnoId,
            state = meetingState,
            scheduled_date = meetingDate,
            title = meetingTitle,
            description = meetingDescription,
            aula = "",
            centro = ""
        )
    }


    companion object {
        //instanciamos lor agrumentos que necesitamos para mostrar el detalle
        private const val ARG_MEETING_ID = "arg_meeting_id"
        private const val ARG_PROFE_ID = "arg_profe_id"
        private const val ARG_ALUMNO_ID = "arg_alumno_id"
        private const val ARG_STATE = "arg_state"
        private const val ARG_DATE = "arg_date"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_AULA = "arg_aula"
        private const val ARG_CENTRO = "arg_centro"




        // le pasamos el user en cada click para rellenar los argumentos dinamicamente
        fun newInstance(selectedMeeting: Meeting): MeetingsDetailFragment {
            val fragment = MeetingsDetailFragment()
            val args = Bundle()
            args.putInt(ARG_MEETING_ID, selectedMeeting.id)
            args.putInt(ARG_PROFE_ID, selectedMeeting.profesor_id)
            args.putInt(ARG_ALUMNO_ID, selectedMeeting.alum)
            args.putString(ARG_STATE, selectedMeeting.state)
            args.putString(ARG_DATE, selectedMeeting.scheduled_date)
            args.putString(ARG_TITLE, selectedMeeting.title)
            args.putString(ARG_DESCRIPTION, selectedMeeting.description)
            args.putString(ARG_DESCRIPTION, selectedMeeting.description)
            args.putString(ARG_AULA, selectedMeeting.aula)
            args.putString(ARG_CENTRO, selectedMeeting.centro)

            fragment.arguments = args

            return fragment
        }
    }
    private fun initUi() {
        binding.tvTitle.text = selectedMeeting.title
        binding.tvDescription.text = selectedMeeting.description
        binding.tvDate.text = selectedMeeting.scheduled_date.substring(0,10)
        binding.tvHour.text = selectedMeeting.scheduled_date.substring(11,16)
        binding.tvState.text = selectedMeeting.state

        if (currentUser.type.id == 3 )
            binding.teacherButtonsLayout.visibility = View.VISIBLE
        else{
            binding.teacherButtonsLayout.visibility = View.GONE
        }

        initListeners()
    }

    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            selectedMeeting.state = "aceptada"
            meetingsVM.updateMeeting(selectedMeeting, currentUser)

            dismiss()
        }
        binding.btnDecline.setOnClickListener {
            selectedMeeting.state = "denegada"
            meetingsVM.updateMeeting(selectedMeeting, currentUser)

            dismiss()
        }

    }

    private fun setupMap() {
        // Configuración necesaria para OSMDroid
        Configuration.getInstance().load(requireContext(),
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()))

        binding.mapView.apply {
            setMultiTouchControls(true)
            setBuiltInZoomControls(false) // Para que no salgan botones feos de +/-

            val mapController = controller
            mapController.setZoom(18.0)

            // Coordenadas aproximadas de Elorrieta-Errekamari (Bilbao)
            val elorrietaPoint = GeoPoint(43.2831, -2.9653)
            mapController.setCenter(elorrietaPoint)

            // Añadir el pin (marcador)
            val startMarker = Marker(this)
            startMarker.position = elorrietaPoint
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            startMarker.title = "Elorrieta-Errekamari"
            overlays.add(startMarker)
        }
    }


    // Es MUY importante gestionar el ciclo de vida del mapa
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
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