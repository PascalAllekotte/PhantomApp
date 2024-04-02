package de.syntax.androidabschluss.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel


class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel: FirebaseViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auoraColor = ContextCompat.getColor(requireContext(), R.color.blau)
        val snowColor = ContextCompat.getColor(requireContext(), R.color.weiß)
        val matrixColor = ContextCompat.getColor(requireContext(), R.color.grün)
        val roseColor = ContextCompat.getColor(requireContext(), R.color.rose)


        binding.stylegroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbblure -> sharedViewModel.updatecolor(auoraColor)
                R.id.rbgreen -> sharedViewModel.updatecolor(matrixColor)
                R.id.rbwhite -> sharedViewModel.updatecolor(snowColor)
                R.id.rbrose -> sharedViewModel.updatecolor(roseColor)


            }
        }

        binding.logout.setOnClickListener {
            viewmodel.logout()
            findNavController().navigate(R.id.homeFragment)
        }


    }
}



