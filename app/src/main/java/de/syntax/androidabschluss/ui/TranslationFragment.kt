package com.example.random.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.FragmentTranslationBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TranslationFragment : Fragment() {

    private lateinit var binding: FragmentTranslationBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.vocabularyRecyclerView.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.vocabularyRecyclerView)

        val vocabularyList = mutableListOf(
            VocabItem("Englisch", "Apple"),
            VocabItem("Englisch", "House"),
            VocabItem("Englisch", "Kenan")



            // Weitere Vokabeln hinzufügen...
        )

        val adapter = VocableAdapter(vocabularyList)
        binding.vocabularyRecyclerView.adapter = adapter
    }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }
    }



