package com.example.random.ui


import de.syntax.androidabschluss.viewmodel.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        if (auth.currentUser != null) {
            // Stelle sicher, dass du im Navigation-Graphen eine Aktion oder einen globalen Übergang hast, die/es dich von der Startdestination zum mainFragment bringt
            findNavController().navigate(R.id.mainFragment)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()

        }
        setupObservers()
        setupLoginButton()
    }

    private fun setupObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                anmeldenErfolgreich(user)
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginViewModel.LoginState.SUCCESS -> {
                    // Hier könntest du eine Erfolgsmeldung anzeigen oder weitere Aktionen durchführen
                }
                is LoginViewModel.LoginState.ERROR -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun setupLoginButton() {
        binding.btnLoginlog.setOnClickListener {
            val email = binding.logmail.text.toString().trim()
            val password = binding.logpassword.text.toString().trim()
            val vibrationAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anima)
            it.startAnimation(vibrationAnimation)
            viewModel.loginUser(email, password)
        }
    }

    private fun anmeldenErfolgreich(user: FirebaseUser) {
        // Hier könntest du den Benutzer weiterleiten oder andere Aktionen ausführen
        val bundle = Bundle().apply {
            putParcelable("user", user)
        }
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }
}
