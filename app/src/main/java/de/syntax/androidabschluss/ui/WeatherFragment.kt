package de.syntax.androidabschluss.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import de.syntax.androidabschluss.adapter.ForeCastAdapter
import de.syntax.androidabschluss.data.model.open.CurrentResponseApi
import de.syntax.androidabschluss.data.model.open.ForecastResponseApi
import de.syntax.androidabschluss.databinding.FragmentWeatherBinding
import de.syntax.androidabschluss.viewmodel.WeatherViewModel
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale




class WeatherFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var binding : FragmentWeatherBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForeCastAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt den Listener für Chart-Interaktionen
        binding.chart.setOnChartValueSelectedListener(this)

        // Setzt den Zurück-Button im Toolbar-Layout
        binding.toolbarLayout2.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
        // Setzt den Titel der Toolbar
        binding.toolbarLayout2.titletext.setText("Weather")

        binding.apply {
            var lat = 51.43
            var lon = 6.88
            var name = "Mülheim an der Ruhr"

            // Initialisiert UI Elemente mit aktuellen Wetterdaten
            cityTxt.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric")?.enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if(response.isSuccessful){
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        linearLayout.visibility = View.VISIBLE
                        data?.let {
                            statustxt.text = it.weather?.get(0)?.main ?: "-"
                            humiditytxt.text = it.main?.humidity?.toString() + "%"
                            windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "km"
                            currentTempTxt.text = it.main?.temp?.let { Math.round(it).toString() } + "°"
                            maxTempTxt.text = it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                            minTempTxt.text = it.main?.tempMin?.let { Math.round(it).toString() } + "°"
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            // Setzt den Blur-Effekt für den Hintergrund
            var radius = 10f
            val decorView = requireActivity().window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBACKGROUND = decorView.background

            rootView?.let {
                blueView.setupWith(it, RenderScriptBlur(requireContext()))
                    .setFrameClearDrawable(windowBACKGROUND)
                    .setBlurRadius(radius)
                blueView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline = true
            }

            // Lädt die Wettervorhersage für die angegebenen Koordinaten und Einheiten
            weatherViewModel.loadForeCastWeather(lat, lon, "metric")
                ?.enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                    // Callback-Methode, die bei Erhalt der HTTP-Antwort aufgerufen wird
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        // Überprüft, ob die Serverantwort erfolgreich war
                        if (response.isSuccessful) {
                            val data = response.body() // Extrahiert den Antwortkörper
                            blueView.visibility = View.VISIBLE // Macht das BlueView sichtbar

                            // Verarbeitet die erhaltenen Daten, wenn sie nicht null sind
                            data?.let {
                                forecastAdapter.differ.submitList(it.list) // Aktualisiert die Liste im Adapter
                                forcastView.apply {
                                    layoutManager = LinearLayoutManager( // Setzt den LayoutManager
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter // Verknüpft den Adapter mit der RecyclerView
                                }
                                setChartData() // Aktualisiert die Chart-Daten basierend auf der neuen Vorhersage
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

        }
    }


    override fun onValueSelected(e: Entry?, h: Highlight?) {
        e?.let {
            val index = it.x.toInt()
            if (index >= 0 && index < forecastAdapter.differ.currentList.size) {
                val temperature = forecastAdapter.differ.currentList[index].main?.temp ?: "-"
                val dateMillis = forecastAdapter.differ.currentList[index].dt?.times(1000L) ?: 0L
                // Anpassen des Formats, um Datum und Uhrzeit zu inkludieren
                val dateTime = SimpleDateFormat("EEEE, d MMM yyyy, HH:mm 'Uhr'", Locale.GERMANY).format(Date(dateMillis))
                val displayText = "Temperatur: $temperature°C am $dateTime"
                binding.detailTextView.text = displayText
            }
        }
    }

    override fun onNothingSelected() {
        binding.detailTextView.text = ""  // Löscht das Textfeld, wenn keine Selektion vorliegt
    }


    private fun setChartData() {
        val entries = ArrayList<Entry>()  // Liste für die Datenpunkte
        val dateValues = ArrayList<Long>() // Liste für die UNIX-Zeitstempel der Datenpunkte

        for (i in forecastAdapter.differ.currentList.indices) {
            val temperature = forecastAdapter.differ.currentList[i].main?.temp?.toFloat() ?: 0f
            val dateMillis =
                forecastAdapter.differ.currentList[i].dt?.times(1000L)  // angenommen 'dt' ist der UNIX-Zeitstempel in Sekunden
            entries.add(Entry(i.toFloat(), temperature))
            if (dateMillis != null) {
                dateValues.add(dateMillis)
            }
        }

        val dataSet = LineDataSet(entries, "Temperatur")
        dataSet.fillAlpha = 110
        dataSet.color = Color.WHITE
        dataSet.valueTextColor = Color.MAGENTA
        dataSet.valueTextSize = 12f
        binding.chart.xAxis.textColor = Color.WHITE
        binding.chart.axisLeft.textColor = Color.WHITE
        binding.chart.axisRight.textColor = Color.WHITE
        binding.chart.legend.textColor = Color.WHITE


        val lineData = LineData(dataSet)
        binding.chart.data = lineData

        // Setzen des ValueFormatters für die X-Achse
        binding.chart.xAxis.valueFormatter = DayAxisValueFormatter(dateValues)
        binding.chart.xAxis.granularity = 1f  // Stellt sicher, dass jeder Index ein Label erhält

        binding.chart.invalidate() // Aktualisiert das Diagramm
    }



}


class DayAxisValueFormatter(private val dates: List<Long>) : ValueFormatter() {
    private val dateFormatter = SimpleDateFormat("EE", Locale.GERMANY)

    override fun getFormattedValue(value: Float): String {
        // value ist der Index des Datenpunkts, der in eine Position in der Liste `dates` umgewandelt wird.
        return if (value.toInt() >= 0 && value.toInt() < dates.size) {
            dateFormatter.format(dates[value.toInt()])
        } else {
            ""
        }
    }
}
