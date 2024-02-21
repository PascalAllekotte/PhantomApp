package com.example.random.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.random.data.model.Message1
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.syntax.androidabschluss.databinding.FragmentReadMessasgeBinding

class ReadMessagesFragment : Fragment() {
    private var _binding: FragmentReadMessasgeBinding? = null // Corrected class name
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView // Defined in class scope
    private lateinit var messages: MutableList<Message1>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReadMessasgeBinding.inflate(inflater, container, false) // Corrected class name
        val view = binding.root

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        messages = mutableListOf()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("messages")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.clear()


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ReadMessagesFragment", "Failed to read value.", error.toException())
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
