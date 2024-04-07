package de.syntax.androidabschluss.data.repositorys

import de.syntax.androidabschluss.data.remote.ApiInterface

class WeatherRepository(val api: ApiInterface) {
    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        api.getCurrentWeather(lat, lng, unit, "4b5f74f98ee4b78a3e1cb4a874811ac3")


    fun getForecastWeather(lat: Double, lng: Double, unit: String) =
        api.getForecastWeather(lat, lng, unit, "4b5f74f98ee4b78a3e1cb4a874811ac3")
}