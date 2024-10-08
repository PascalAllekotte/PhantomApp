package de.syntax.androidabschluss.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import de.syntax.androidabschluss.adapter.PictureAdapter
import de.syntax.androidabschluss.databinding.FragmentPicturesBinding
import de.syntax.androidabschluss.viewmodel.PicturesViewModel


class PicturesFragment : Fragment() {

    private lateinit var binding: FragmentPicturesBinding
    private lateinit var pictureAdapter: PictureAdapter
    private val picturesViewModel: PicturesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPicturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout2.titletexttool.setText("Pictures")
        binding.toolbarLayout2.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
        pictureAdapter = PictureAdapter(
            { position, pictureItem ->
                //für einen click
            },
            { position, pictureItem ->
                AlertDialog.Builder(context).apply {
                    setTitle("Bild löschen")
                    setMessage("Möchten Sie dieses Bild wirklich löschen?")
                    setPositiveButton("Löschen") { dialog, which ->
                        picturesViewModel.delete(pictureItem)
                    }
                    setNegativeButton("Abbrechen", null)
                }.show()
            }
        )

        binding.imageRv.apply {
            adapter = pictureAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        picturesViewModel.allPictures.observe(viewLifecycleOwner) { pictures ->
            pictureAdapter.submitList(pictures.reversed())
        }
    }

    }
