package com.example.random.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.syntax.androidabschluss.MainActivity
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

private lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE


        binding.btnLogin.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment2)


        }
        binding.btnRegistration.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_registrationFragment)
        }
    }


}