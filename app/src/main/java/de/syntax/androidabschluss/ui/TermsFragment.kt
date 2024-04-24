package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.TermAdapter
import de.syntax.androidabschluss.databinding.FragmentTermsBinding
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import de.syntax.androidabschluss.viewmodel.TermViewModel


class TermsFragment : Fragment() {

    private lateinit var binding : FragmentTermsBinding
    private lateinit var termAdapter: TermAdapter
    private lateinit var termViewModel: TermViewModel
    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTermsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.toolbarLayout.titletext.setText("Terms")
        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addbutton.setOnClickListener {
            findNavController().navigate(R.id.termDetailDetailFragment)
        }

        termViewModel = ViewModelProvider(this).get(TermViewModel::class.java)
        setUpTermRecyclerview()


        termViewModel.termList.observe(viewLifecycleOwner) { termList ->
            termAdapter.updateList(termList)

        }
        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { color ->
            termAdapter.updateStrokeColor(color)
        }


    }

    private fun setUpTermRecyclerview() {
        termAdapter = TermAdapter(mutableListOf()) { termItem ->
            termViewModel.deleteTermItem(termItem)
        }

        binding.termRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.termRecyclerView.adapter = termAdapter
        LinearSnapHelper().attachToRecyclerView(binding.termRecyclerView)

    }


}



