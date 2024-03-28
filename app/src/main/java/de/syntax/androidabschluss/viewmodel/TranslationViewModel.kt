package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.syntax.androidabschluss.adapter.Request.DeeplRequest
import de.syntax.androidabschluss.data.remote.ApiClientDeepL
import de.syntax.androidabschluss.data.repositorys.TranslationRepository
import de.syntax.androidabschluss.response.DeeplResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslationViewModel : ViewModel() {

    private val repository = TranslationRepository(ApiClientDeepL.getInstance())

    private val _translation = MutableLiveData<String>()
    val translation: LiveData<String>
        get() = _translation

    fun translateText(text: String) {
        val request = DeeplRequest(target_lang = "DE", text = listOf(text))
        repository.translateText(request).enqueue(object : Callback<DeeplResponse> {
            override fun onResponse(call: Call<DeeplResponse>, response: Response<DeeplResponse>) {
                if (response.isSuccessful) {
                    _translation.value = response.body()?.translations?.firstOrNull()?.text ?: ""
                } else {
                    _translation.value = "Ein Fehler ist aufgetreten"
                }
            }

            override fun onFailure(call: Call<DeeplResponse>, t: Throwable) {
                _translation.value = "Netzwerkfehler: ${t.message}"
            }
        })
    }
}
