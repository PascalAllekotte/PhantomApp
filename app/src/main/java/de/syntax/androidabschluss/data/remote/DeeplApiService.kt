package de.syntax.androidabschluss.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax.androidabschluss.data.model.TranslationRequest
import de.syntax.androidabschluss.data.model.TranslationResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

const val BASE_URL = "https://api-free.deepl.com/v2/"

const val API_TOKEN = "48329d96-cd7d-4bc4-ac7f-f9d0e666f2f9:fx"

val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()
        .addHeader("Authorization", "DeepL-Auth-Key $API_TOKEN")
        .build()
    chain.proceed(newRequest)
}.build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DeepLApiService {
    @POST
    suspend fun translateText(
        @Url url: String,
        @Header("User-Agent") userAgent: String,
        @Body requestBody: TranslationRequest
    ): TranslationResponse
}

object DeepLApi {
    val retrofitService: DeepLApiService by lazy {
        retrofit.create(DeepLApiService::class.java)
    }
}