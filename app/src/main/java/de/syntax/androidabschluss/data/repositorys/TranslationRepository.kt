package de.syntax.androidabschluss.data.repositorys

import de.syntax.androidabschluss.data.Request.DeeplRequest
import de.syntax.androidabschluss.data.remote.ApiInterface
import de.syntax.androidabschluss.response.DeeplResponse
import retrofit2.Call

class TranslationRepository(private val apiInterface: ApiInterface) {

    fun translateText(deeplRequest: DeeplRequest): Call<DeeplResponse> {
        return apiInterface.deepLTranslate(deeplRequest)
    }
}
