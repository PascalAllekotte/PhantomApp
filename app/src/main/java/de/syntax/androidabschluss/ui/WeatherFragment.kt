package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.data.model.open.CurrentResponseApi
import de.syntax.androidabschluss.databinding.FragmentWeatherBinding
import de.syntax.androidabschluss.viewmodel.WeatherViewModel
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar


class WeatherFragment : Fragment() {

    private lateinit var binding : FragmentWeatherBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarLayout2.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarLayout2.titletext.setText("Weather")


        binding.apply {
            var lat = 51.43
            var lon = 6.88
            var name = "Mülheim an der Ruhr"

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


                           // val drawable=if(isNightNow())
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

    private fun isNightNow():Boolean{
    return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    /**
     *

    private fun setDynamicallyWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
            }

        }
    }



    private fun initWeatherView(type: PrecipType){

        binding.weatherView.apply {
            setWeatherData(type)
            angle=-20
            emissionRate=100.0f

        }

    }
**/
}