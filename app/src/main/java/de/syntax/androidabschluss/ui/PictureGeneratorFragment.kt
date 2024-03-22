package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import de.syntax.androidabschluss.databinding.FragmentPictureGeneratorBinding
import de.syntax.androidabschluss.utils.hideKeyBoard
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.viewmodel.ImageGenerationViewModel



class PictureGeneratorFragment : Fragment() {
    private lateinit var binding: FragmentPictureGeneratorBinding
    private val imageGenerationViewModel: ImageGenerationViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureGeneratorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.anzahlListe.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..10).toList()
            )
        )

        binding.btGenerate.setOnClickListener {
            view.context.hideKeyBoard(it)

            if (binding.etInput.text.toString().trim().isNotEmpty()) {
                if (binding.etInput.text.toString().trim().isNotEmpty()){

                    if (binding.etInput.text.toString().trim().length < 1000 ){
                        Log.d("etinput", binding.etInput.text.toString().trim())
                        Log.d("anzahlListe", binding.anzahlListe.text.toString().trim())

                        val selectedSizeRB = binding.imageGrE.checkedRadioButtonId
                        Log.d( "selectedSizeRB", binding.root.findViewById<RadioButton>(selectedSizeRB).text.toString().trim())


                        //      binding.etInput.text = null

                    }else{
                        view.context.longToastShow("auswahl machen")

                    }
                }
            } else {
                view.context.longToastShow("auswahl machen")
            }
        }


        binding.backbutton.setOnClickListener {
            findNavController().popBackStack()


        }

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