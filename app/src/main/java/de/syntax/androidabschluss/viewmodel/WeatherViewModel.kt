package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.ViewModel
import de.syntax.androidabschluss.data.remote.ApiClientWeather
import de.syntax.androidabschluss.data.remote.ApiInterface
import de.syntax.androidabschluss.data.repositorys.WeatherRepository

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {
    constructor() : this(WeatherRepository(ApiClientWeather().getClient().create(ApiInterface::class.java)))

    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        repository.loadCurrentWeather(lat, lng, unit)

    fun loadForeCastWeather(lat: Double, lng: Double, unit: String) =
        repository.getForecastWeather(lat, lng, unit)

}
