package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentLoginBinding
import com.example.notesapp.models.UserRequest
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.TokenManger
import com.example.notesapp.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManger: TokenManger

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val result = validateUserInput()
            if (result.first) {
                authViewModel.loginUser(getRequest())
            } else {
                binding.tvError.text = result.second
            }
        }
        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObserver()
    }

    private fun getRequest(): UserRequest {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        return UserRequest(email, password, "")
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userInput = getRequest()
        return authViewModel.validateCredentials("", userInput.email, userInput.password, true)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManger.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
