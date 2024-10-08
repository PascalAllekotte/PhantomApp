package de.syntax.androidabschluss.ui


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import de.syntax.androidabschluss.adapter.TermAdapter
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.data.model.open.response.CurrentResponseApi
import de.syntax.androidabschluss.data.model.open.response.ForecastResponseApi
import de.syntax.androidabschluss.databinding.FragmentMainBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import de.syntax.androidabschluss.viewmodel.TermViewModel
import de.syntax.androidabschluss.viewmodel.VokabelViewModel
import de.syntax.androidabschluss.viewmodel.WeatherViewModel
import retrofit2.Call
import retrofit2.Response


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    private lateinit var vocableAdapter: VocableAdapter
    private lateinit var vokabelViewModel: VokabelViewModel

    private lateinit var noteAdapter: NoteAdapter // xxxxxx
    private lateinit var noteViewModel: NoteViewModel  // xxxx

    private lateinit var termAdapter: TermAdapter
    private lateinit var termViewModel: TermViewModel

    //Wetter
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val forecastAdapter by lazy { ForeCastAdapter() }





    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
            weatherViewModel.loadCurrentWeather(lat, lon, "metric")?.enqueue(object :
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




            // forecast temp
            weatherViewModel.loadForeCastWeather(lat, lon,  "metric")
                ?.enqueue(object : retrofit2.Callback<ForecastResponseApi> {
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
            termAdapter.updateStrokeColor(color)
            val colorStateList = ColorStateList.valueOf(color)
            binding.blueView.setStrokeColor(colorStateList)

        }

        termViewModel = ViewModelProvider(this).get(TermViewModel::class.java)
        setUpTermRecyclerview()


        termViewModel.termList.observe(viewLifecycleOwner) { termList ->
            // Begrenze die Liste auf die ersten zwei Einträge
            val limitedList = if (termList.size > 2) termList.take(2) else termList
            termAdapter.updateList(limitedList)
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

    private fun setUpTermRecyclerview() {
        termAdapter = TermAdapter(mutableListOf()) { termItem ->
            termViewModel.deleteTermItem(termItem)
        }

        binding.termRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.termRecyclerView.adapter = termAdapter
        LinearSnapHelper().attachToRecyclerView(binding.termRecyclerView)

    }



}