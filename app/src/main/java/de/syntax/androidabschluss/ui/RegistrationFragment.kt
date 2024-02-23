package com.example.random.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentRegistrationBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel


// RegistrationFragment
class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val email = binding.regmail.text.toString()
            val password = binding.regpassword.text.toString()
            val name = binding.regname.text.toString()
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(context, "Bitte erst Eingabe machen!!!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(email, password, name)
                viewModel.currentUser.observe(viewLifecycleOwner) { user ->
                    if (user != null) {
                        navigateToHomeFragment()
                    }
                }
            }
        }

        return binding.root
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }
}
