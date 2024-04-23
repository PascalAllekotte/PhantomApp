package com.example.random.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)


        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.button.setOnClickListener {
            val email = binding.regmail.text.toString()
            val password = binding.regpassword.text.toString()
            val password2 = binding.regpassword2.text.toString()
            val name = binding.regname.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || password2.isEmpty()) {
                Toast.makeText(context, "Bitte erst Eingabe machen!!!", Toast.LENGTH_SHORT).show()
            } else if (password != password2) {
                Toast.makeText(context, "Die Passwörter stimmen nicht überein.", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            user?.updateProfile(
                                UserProfileChangeRequest.Builder().setDisplayName(name).build()
                            )
                            saveUsernameToDatabase(user?.uid, name)
                            updateUI(user)
                            navigateToHomeFragment()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            updateUI(null)
                        }
                    }
            }
        }

        return binding.root

    }

    private fun saveUsernameToDatabase(userId: String?, name: String) {
        if (userId != null) {
            val userRef = databaseReference.child("users").child(userId)
            userRef.setValue(mapOf("username" to name))
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun checkUsernameExists(name: String, callback: (Boolean) -> Unit) {
        databaseReference.child("users").orderByChild("username").equalTo(name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    callback(dataSnapshot.exists())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "checkUsernameExists:onCancelled", databaseError.toException())
                }
            })
    }


}

