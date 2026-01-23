package com.example.cuackapp.ui.Users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cuackapp.R
import com.example.cuackapp.databinding.FragmentUsersBinding
import com.example.cuackapp.model.UserModel.User
import com.example.cuackapp.ui.Home.HomeFragmentDirections
import com.example.cuackapp.ui.NavigationHUD.NavigationActivity
import com.example.cuackapp.ui.Users.Adapter.usersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private var _binding : FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser : User // CORREGIDO
    private lateinit var usersAdapter: usersAdapter

    private val usersVM : UsersViewModel by viewModels()
    private var users: List<User> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = obtenerUsuario() // CORREGIDO
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

                    usersAdapter.updateList(users)
                }
            }
            }
    }


    private fun initUi() {

       initListeners()

        usersAdapter = usersAdapter(mutableListOf()) { user ->
            // Lo que pasa al hacer click en un usuario
            Toast.makeText(context, "Click en ${user.username}", Toast.LENGTH_SHORT).show()
        }

        // 3. Conectarlo al RecyclerView
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
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
            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToProfileFragment()
            )

        }
    }
    private fun obtenerUsuario(): User {
        val activity = requireActivity() as NavigationActivity
        val user = activity.currentUser // Acceso directo a la variable

        return user
    }
}