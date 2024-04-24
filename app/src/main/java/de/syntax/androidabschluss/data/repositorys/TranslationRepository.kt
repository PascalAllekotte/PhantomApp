package de.syntax.androidabschluss.data.repositorys

import de.syntax.androidabschluss.data.model.open.Request.DeeplRequest
import de.syntax.androidabschluss.data.model.open.response.DeeplResponse
import de.syntax.androidabschluss.data.remote.ApiInterface
import retrofit2.Call

class TranslationRepository(private val apiInterface: ApiInterface) {

    fun translateText(deeplRequest: DeeplRequest): Call<DeeplResponse> {
        return apiInterface.deepLTranslate(deeplRequest)
    }
}
