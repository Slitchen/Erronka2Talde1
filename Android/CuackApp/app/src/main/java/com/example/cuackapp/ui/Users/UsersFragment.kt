package com.example.cuackapp.ui.Users

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentUsersBinding
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.example.cuackapp.ui.Users.Adapter.quickAccesAdapter
import com.example.cuackapp.ui.Users.Adapter.usersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private var _binding : FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser : User
    private lateinit var usersAdapter: usersAdapter
    private lateinit var quickAccesAdapter: quickAccesAdapter

    private var selectedCiclo: String = "Todos"
    private var selectedCurso: String = "Todos"


    private val usersVM : UsersViewModel by viewModels()
    private var users: List<User> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        currentUser = obtenerUsuario()
        setupObservers()
        initUi()
        usersVM.getUsers()
    }






    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

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

                    when(currentUser.type.id){
                        1->{   //god
                            var quickAccessUsers = users.filter { user ->
                                user.type.id ==2
                            }
                            quickAccesAdapter.updateList(quickAccessUsers)
                        }
                        2->{//admin
                            var quickAccessUsers = users.filter { user ->
                                user.type.id ==3
                            }
                            quickAccesAdapter.updateList(quickAccessUsers)
                        }
                        3->{//teacher
                            var quickAccessUsers = users.filter { user ->
                                user.type.id ==4
                            }
                            quickAccesAdapter.updateList(quickAccessUsers)
                        }
                        4->{//student
                            var quickAccessUsers = users.filter { user ->

                                 user.type.id == 3
                            }
                            quickAccesAdapter.updateList(quickAccessUsers)
                        }
                    }
                }
            }
            }
    }


    private fun initUi() {

       initListeners()

        usersAdapter = usersAdapter(mutableListOf()) { user ->
            val detailSheet = UserDetailFragment.newInstance(user)
            detailSheet.show(parentFragmentManager, "UserDetail")
        }
        quickAccesAdapter = quickAccesAdapter(mutableListOf()) { user ->
            val detailSheet = UserDetailFragment.newInstance(user)
            detailSheet.show(parentFragmentManager, "UserDetail")
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }
        binding.rvQuickAccess.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = quickAccesAdapter

        }

        if (currentUser.type.id == 4){
            binding.btnFilterCiclo.visibility = GONE
            binding.btnFilterCurso.visibility = GONE
        }



    }

    private fun initListeners() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()
                if (search.length > 0) {
                    var searchedUsers = users.filter { user ->
                        user.username.contains(search)
                    }
                    binding.searchResultsCard.visibility = View.VISIBLE
                    usersAdapter.updateList(searchedUsers)
                } else {
                    binding.searchResultsCard.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {

            }
            override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {}

        }
        )

        binding.btnSettings.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.homeFragment, inclusive = false)
                .build()

            findNavController().navigate(
                R.id.profileFragment,
                null,
                navOptions
            )
        }
        binding.btnFilterCiclo.setOnClickListener {
            showFilterDialog("Seleccionar Ciclo", listOf("Todos", "DAM", "DAW", "ASIR")) { seleccion ->
                selectedCiclo = seleccion
                binding.tvFilterCiclo.text = "Ciclo: $seleccion"
                applyAllFilters()
            }
        }

        binding.btnFilterCurso.setOnClickListener {
            showFilterDialog("Seleccionar Curso", listOf("Todos", "1", "2")) { seleccion ->
                selectedCurso = seleccion
                binding.tvFilterCurso.text = "Curso: $seleccion"
                applyAllFilters()
            }
        }
    }
    private fun applyAllFilters() {
        val searchText = binding.etSearch.text.toString().lowercase()

        val filteredList = users.filter { user ->
            val matchesSearch = user.username.lowercase().contains(searchText)
            val matchesCiclo = selectedCiclo == "Todos" || user.ciclo == selectedCiclo
            val matchesCurso = selectedCurso == "Todos" || user.curso == selectedCurso

            matchesSearch && matchesCiclo && matchesCurso
        }

        usersAdapter.updateList(filteredList)

        binding.searchResultsCard.visibility = if (searchText.isNotEmpty() || selectedCiclo != "Todos" || selectedCurso != "Todos") {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showFilterDialog(title: String, options: List<String>, onSelected: (String) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(options.toTypedArray()) { _, which ->
                onSelected(options[which])
            }
            .show()
    }
    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        val user = activity.currentUser
        return user
    }
}