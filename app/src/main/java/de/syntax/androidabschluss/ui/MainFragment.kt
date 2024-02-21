package com.example.random.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.random.data.model.Message1
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import de.syntax.androidabschluss.MainActivity
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register a FragmentResultListener
        parentFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            if (requestKey == "requestKey") {
                user = bundle.getParcelable("user")
                updateUI()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       
        (activity as? MainActivity)?.findViewById<Toolbar>(R.id.toolbar)?.visibility = View.GONE
        updateUI()



        binding.encrypt.setOnClickListener(){
            updateWhenClicked()
        }

        binding.btnSend.setOnClickListener(){
            val message = binding.editTextTextMultiLine.text.toString()
            val keyword = if (binding.encrypt.isChecked) binding.keyword.text.toString() else ""

            // Get the current user
            user?.let { user ->
                // Create a new message object
                val newMessage = Message1(message, keyword, user.uid)

                // Save the message to the Firebase Realtime Database
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("messages")
                myRef.push().setValue(newMessage)
            }
        }


    }

    private fun updateUI() {
        user?.let {
            binding.username.text = it.displayName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setUser(user: FirebaseUser) {
        this.user = user
        updateUI()
    }
    fun updateWhenClicked() {
        binding.keyword.visibility = if (binding.encrypt.isChecked) View.VISIBLE else View.GONE



    }
}
