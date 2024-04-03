package de.syntax.androidabschluss.ui


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.utils.getAuoraColor
import de.syntax.androidabschluss.utils.getEvilColor
import de.syntax.androidabschluss.utils.getMatrixColor
import de.syntax.androidabschluss.utils.getRoseColor
import de.syntax.androidabschluss.utils.getSnowColor
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



        binding.toolbarLayout2.titletexttool.setText("Settings")
        binding.toolbarLayout2.backbutton.visibility = View.GONE

        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { colorInt ->
            val colorStateList = ColorStateList.valueOf(colorInt)
            binding.cardviewtr.setStrokeColor(colorStateList)

        }


        binding.stylegroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbblure -> sharedViewModel.updatecolor(getAuoraColor(requireContext()))
                R.id.rbgreen -> sharedViewModel.updatecolor(getMatrixColor(requireContext()))
                R.id.rbwhite -> sharedViewModel.updatecolor(getSnowColor(requireContext()))
                R.id.rbrose -> sharedViewModel.updatecolor(getRoseColor(requireContext()))
                R.id.rbred -> sharedViewModel.updatecolor(getEvilColor(requireContext()))



            }
        }

        binding.logout.setOnClickListener {
            viewmodel.logout()
            findNavController().navigate(R.id.homeFragment)
        }


    }
}



