package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.data.model.TranslationRequest
import de.syntax.androidabschluss.data.model.TranslationResponse
import de.syntax.androidabschluss.data.remote.DeepLApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DeepLRepository(private val api: DeepLApiService) {
    private val _translations = MutableLiveData<TranslationResponse>()
    val translations: LiveData<TranslationResponse>
        get() = _translations

    // Funktion zum Abrufen von Übersetzungen
    suspend fun getTranslations(authKey: String, text: String, targetLang: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                api.translateText(
                    url = "https://api-free.deepl.com/v2/translate",
             //       authKey = "DeepL-Auth-Key $authKey",
                    userAgent = "YourApp/1.2.3",
                    requestBody = TranslationRequest(listOf(text), targetLang)
                )
            }
            _translations.postValue(response)
        } catch (e: Exception) {
            Log.e("TAG","Fehler", e)
        }
    }
}
