package de.syntax.androidabschluss.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ForeCastAdapter
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.data.model.open.CurrentResponseApi
import de.syntax.androidabschluss.data.model.open.ForecastResponseApi
import de.syntax.androidabschluss.databinding.FragmentMainBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import de.syntax.androidabschluss.viewmodel.UserSettingsViewModel
import de.syntax.androidabschluss.viewmodel.VokabelViewModel
import de.syntax.androidabschluss.viewmodel.WeatherViewModel
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var vocableAdapter: VocableAdapter
    private lateinit var vokabelViewModel: VokabelViewModel

    private lateinit var noteAdapter: NoteAdapter // xxxxxx
    private lateinit var noteViewModel: NoteViewModel  // xxxx

    //Wetter
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForeCastAdapter() }

    //Setting instanz setzten und laden
    private val userSettingsViewModel: UserSettingsViewModel by viewModels()





    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            var lat = 51.43
            var lon = 6.88
            var name = "Mülheim an der Ruhr"

            // current temperature
            cityTxt.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {

                    if(response.isSuccessful){
                        val data=response.body()
                        progressBar.visibility=View.GONE
                        linearLayout.visibility=View.VISIBLE
                        data?.let {
                            statustxt.text=
                                it.weather?.get(0)?.main ?: "-"
                            humiditytxt.text=it.main?.humidity?.toString()+"%"
                            windTxt.text =
                                it.wind?.speed?.let { Math.round(it).toString() }+"km"
                            currentTempTxt.text=
                                it.main?.temp?.let { Math.round(it).toString() }+"°"
                            maxTempTxt.text=
                                it.main?.tempMax?.let { Math.round(it).toString() }+"°"
                            minTempTxt.text=
                                it.main?.tempMin?.let { Math.round(it).toString() }+"°"



                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

            //Setting Blue View
            var radius=10f
            val decorView = requireActivity().window.decorView
            val rootView=(decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBACKGROUND=decorView.background

            rootView?.let {
                blueView.setupWith(it, RenderScriptBlur(requireContext())) // eventuell fehler
                    .setFrameClearDrawable(windowBACKGROUND)
                    .setBlurRadius(radius)
                blueView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline = true
            }

            // forecast temp
            weatherViewModel.loadForeCastWeather(lat, lon,  "metric")
                .enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            blueView.visibility = View.VISIBLE

                            data?.let {
                                forecastAdapter.differ.submitList(it.list)
                                forcastView.apply {
                                    layoutManager = LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                    }
                })



        }

        binding.weatherBtn.setOnClickListener {
            findNavController().navigate(R.id.weatherFragment)
        }

        binding.toolbarLayout.backbutton.visibility = View.GONE
        binding.toolbarLayout.titletexttool.setText("Main")

        vokabelViewModel = ViewModelProvider(this).get(VokabelViewModel::class.java)
        setupRecyclerView()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setupRecyclerViewNote() // xxxx

        vokabelViewModel.vokabelList.observe(viewLifecycleOwner) { vocabularyList ->
            val favorite = vocabularyList.filter { it.favorite }
            vocableAdapter.updateList(favorite)
        }

        // Zeigt die letzten Einträge als erstres
        noteViewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            noteAdapter.updateList(noteList)
        } //xxx

        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { color ->
            noteAdapter.updateStrokeColor(color)
            vocableAdapter.updateStrokeColor(color)
        }
    }

    private fun setupRecyclerView() {
        vocableAdapter = VocableAdapter(mutableListOf()) { vocabItem ->
        }
        binding.vocabularyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.vocabularyRecyclerView.adapter = vocableAdapter
        LinearSnapHelper().attachToRecyclerView(binding.vocabularyRecyclerView)
    }

    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            noteViewModel.delete(noteItem)
        }

        //----------------Note--------------
        binding.noterecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noterecyclerView.adapter = noteAdapter
        binding.noterecyclerView.setHasFixedSize(true)
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}