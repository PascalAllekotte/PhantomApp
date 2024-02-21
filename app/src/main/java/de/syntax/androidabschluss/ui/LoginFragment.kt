package com.example.random.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentLoginBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel

class LoginFragment : Fragment() {

    // Zugriff auf ViewModel
    private val viewModel: FirebaseViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding // ViewBinding
    private lateinit var auth: FirebaseAuth // Firebase authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            binding.btnLoginlog.setOnClickListener {
                val email = binding.logmail.text.toString()
                val password = binding.logpassword.text.toString()

                if (binding.logpassword.text.isNullOrEmpty() || binding.logmail.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Bitte erst Eingabe machen!!!", Toast.LENGTH_SHORT).show()
                } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Anmelden erfolgreich!")
                            val user = auth.currentUser
                            anmeldenErfolgreich()
                            updateUI(user)
                        } else {
                            Log.w(TAG, "Anmelden fehlgeschlagen!", task.exception)
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val userName = user?.displayName
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

    fun anmeldenErfolgreich(){
        auth.currentUser?.let { user ->
            val bundle = Bundle().apply {
                putParcelable("user", user)
            }
            parentFragmentManager.setFragmentResult("requestKey", bundle)

            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

}
