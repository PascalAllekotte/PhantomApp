package de.syntax.androidabschluss.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.ImageGenerationViewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel : FirebaseViewModel by activityViewModels()
    private val imageGenerationViewModel: ImageGenerationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageGenerationViewModel.imageUrl.observe(viewLifecycleOwner) { imgUrl ->
            if (imgUrl != null) {
                loadImage(imgUrl)
            } else {
                // Handle error
            }
        }

        imageGenerationViewModel.isInProgress.observe(viewLifecycleOwner) { isInProgress ->
            setInProgress(isInProgress)
        }


        binding.logout.setOnClickListener{
            viewmodel.logout()
            findNavController().navigate(R.id.homeFragment)
        }


        binding.btGenerate.setOnClickListener {
            val text = binding.etInput.text.toString()
            if (text.isEmpty()) {
                binding.etInput.setError("No Empty Text")
            } else {
                imageGenerationViewModel.generateImage(text)
            }
        }
    }

    private fun loadImage(imgUrl: String) {
        Picasso.get().load(imgUrl).into(binding.imageView)
        binding.imageView.visibility = View.VISIBLE
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.progressbar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.btGenerate.visibility = if (inProgress) View.GONE else View.VISIBLE
    }
}



