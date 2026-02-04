package com.example.cuackapp.ui.Meetings.DetailsDialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cuackapp.databinding.FragmentCreateMeetingsDetailBinding
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.example.cuackapp.ui.Users.Adapter.usersAdapter
import com.example.cuackapp.ui.Meetings.MeetingsViewModel
import com.example.cuackapp.ui.Users.UsersViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class CreateMeetingsDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreateMeetingsDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersAdapter: usersAdapter
    private val usersVM : UsersViewModel by viewModels()
    private val meetingsVM : MeetingsViewModel by viewModels()

    private lateinit var users: List<User>
    private lateinit var currentUser : User
    private var selectedUser : User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMeetingsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val bottomSheet = binding.root.parent as? View
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = obtenerUsuario()
        initUI()
    }

    private fun initUI() {

        setupObservers()
        initListeners()
        setupSpinners()

        usersAdapter = usersAdapter(mutableListOf()) { user ->
            binding.etUser.setText(user.username)
            selectedUser = user
        }
        binding.rvUsersCreateMeeting.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = usersAdapter

        }
        usersVM.getUsers()

    }

    private fun initListeners() {
        binding.etUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()

                    var searchedUsers = users.filter { user ->
                        user.username.contains(search)
                    }

                    usersAdapter.updateList(searchedUsers)


            }
            override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {

            }
            override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {}

        }
        )

        binding.btnCreateMeeting.setOnClickListener {
            if (
                binding.etTitle.text.isNullOrEmpty() ||
                binding.etDescription.text.isNullOrEmpty() ||
                binding.etUser.text.isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "¡Rellena todos los campos correctamente!", Toast.LENGTH_LONG).show()


            }else{
            if (selectedUser == null) {
             var selectedUsers = users.filter { user ->
                 user.username.contains(binding.etUser.text.toString())

                 }
                if(selectedUsers.isNotEmpty()){
                    selectedUser = selectedUsers.get(0)
                }
            }
            if (selectedUser != null) {
                createMeeting(selectedUser!!)
                Log.i("CreateMeeting", "Creating meeting with user: ${selectedUser?.username}")
            }

        }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Aquí "escuchamos" los cambios de la lista
                usersVM.users.collect { allUsers ->

                    users = allUsers.filter { user ->
                        user.id != currentUser.id
                    }
                    when(currentUser.type.id){
                        3 -> { //teacher
                            users = users.filter { user ->
                                user.type.id ==4
                            }
                        }
                        4 -> { //student
                            users = users.filter { user ->
                                user.type.id ==3
                            }
                        }
                    }

                    usersAdapter.updateList(users)
                }
            }
        }
    }
    private fun setupSpinners() {
        val horasPermitidas = listOf("08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00")
        val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

        val dateAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresDias)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDate.adapter = dateAdapter

        val hourAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horasPermitidas)
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHour.adapter = hourAdapter
    }


    private fun createMeeting(selectedUser: User) {
        val diasPermitidos = listOf("2026-01-26", "2026-01-27", "2026-01-28", "2026-01-29", "2026-01-30")
        val horasPermitidas = listOf("08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00")
        // Obtenemos el índice de lo que el usuario ha pulsado (0 para Lunes, 1 para Martes...)
        val indexDia = binding.spinnerDate.selectedItemPosition
        val indexHora = binding.spinnerHour.selectedItemPosition

        // Sacamos los valores reales de tus listas usando ese índice
        val fechaReal = diasPermitidos[indexDia]
        val horaReal = horasPermitidas[indexHora]

        val newmeeting = Meeting(
            id = 0,
            state = "pendiente",
            profesor_id = 0,
            alum = 0,
            // Combinamos: "2026-01-26 08:00:00"
            scheduled_date = "$fechaReal $horaReal",
            title = binding.etTitle.text.toString(),
            description = binding.etDescription.text.toString(),
            aula = "Aula 101",
            centro = "15112"
        )
        if (currentUser.type.id == 3) { // Profesor
            newmeeting.profesor_id = currentUser.id
            newmeeting.alum = selectedUser.id
        } else { // Alumno
            newmeeting.alum = currentUser.id
            newmeeting.profesor_id = selectedUser.id
        }

        meetingsVM.createMeeting(newmeeting)
        dismiss()
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


