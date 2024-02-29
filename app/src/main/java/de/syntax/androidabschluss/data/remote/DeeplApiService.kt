package de.syntax.androidabschluss.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax.androidabschluss.data.model.DeepLResponse
import de.syntax.androidabschluss.data.repositorys.DeepLRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

const val BASE_URL = "https://api-free.deepl.com/"
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

interface UserApiService {
    @POST("v2/translate")
    @Headers("Content-Type: application/json")
    suspend fun translateText(
        @Body requestBody: Map<String, Any>
    ): Response<DeepLResponse>

}

object UserApi {
    val userApiService: UserApiService = retrofit.create(UserApiService::class.java)
    val deepLRepository = DeepLRepository(userApiService)
}