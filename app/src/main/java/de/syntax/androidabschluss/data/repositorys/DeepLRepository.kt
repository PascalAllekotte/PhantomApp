package de.syntax.androidabschluss.data.repositorys

import de.syntax.androidabschluss.adapter.Request.DeeplRequest
import de.syntax.androidabschluss.data.remote.ApiClientDeepL
import de.syntax.androidabschluss.response.DeeplResponse
import retrofit2.Call

class DeepLRepository {
    private val apiClient = ApiClientDeepL.getInstance()

    suspend fun translateText(deeplRequest: DeeplRequest): Call<DeeplResponse> {
        return apiClient.deepLTranslate(deeplRequest, "application/json", "48329d96-cd7d-4bc4-ac7f-f9d0e666f2f9:fx")
    }
}
