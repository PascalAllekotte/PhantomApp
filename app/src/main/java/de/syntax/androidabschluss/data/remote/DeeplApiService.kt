package de.syntax.androidabschluss.data.remote

import android.view.translation.TranslationResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

const val BASE_URL = "https://api-free.deepl.com/"



interface DeepLService {
    @Headers("Authorization: DeepL-Auth-Key 48329d96-cd7d-4bc4-ac7f-f9d0e666f2f9:fx")
    @POST("v2/translate")
    suspend fun translateText(@Body request: TranslateRequest): Response<TranslateResponse>
}


object DeepLClient {
    private const val BASE_URL = "https://api-free.deepl.com"

    val instance: DeepLService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DeepLService::class.java)
    }
}

data class TranslateRequest(
    val text: List<String>,
    val target_lang: String
)

data class TranslateResponse(
    val translations: List<Translation>
)

data class Translation(
    val detected_source_language: String,
    val text: String
)