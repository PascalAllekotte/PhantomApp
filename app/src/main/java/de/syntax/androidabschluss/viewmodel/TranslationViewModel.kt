package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.remote.DeepLClient
import de.syntax.androidabschluss.data.remote.TranslateRequest
import kotlinx.coroutines.launch

class TranslationViewModel : ViewModel() {
    fun translateText(text: String) {
        viewModelScope.launch {
            val response = DeepLClient.instance.translateText(
                TranslateRequest(listOf(text), "DE")
            )

            if (response.isSuccessful) {
                val translation = response.body()?.translations?.first()?.text
                // Verarbeiten Sie die Übersetzung hier
            } else {
                // Fehlerbehandlung hier
            }
        }
    }
}