package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import de.syntax.androidabschluss.data.remote.ApiInterface

class WeatherRepository(val api: ApiInterface) {

    // Lädt das aktuelle Wetter anhand von geografischen Koordinaten und der gewünschten Maßeinheit
    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        try {
            api.getCurrentWeather(lat, lng, unit, "4b5f74f98ee4b78a3e1cb4a874811ac3")
        } catch (e: Exception) {
            // Loggt und behandelt Fehler bei der Abfrage des aktuellen Wetters
            Log.e("WeatherRepository", "Error loading current weather", e)
            null
        }

    // Lädt die Wettervorhersage für die nächsten Tage anhand von geografischen Koordinaten und der gewünschten Maßeinheit
    fun getForecastWeather(lat: Double, lng: Double, unit: String) =
        try {
            api.getForecastWeather(lat, lng, unit, "4b5f74f98ee4b78a3e1cb4a874811ac3")
        } catch (e: Exception) {
            // Loggt und behandelt Fehler bei der Abfrage der Wettervorhersage
            Log.e("WeatherRepository", "Error loading forecast weather", e)
            null
        }
}
