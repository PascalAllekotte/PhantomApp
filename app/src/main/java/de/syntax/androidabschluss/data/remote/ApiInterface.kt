package de.syntax.androidabschluss.data.remote

import de.syntax.androidabschluss.BuildConfig.OPENAI_API_KEY
import de.syntax.androidabschluss.adapter.Request.DeeplRequest
import de.syntax.androidabschluss.data.model.open.CurrentResponseApi
import de.syntax.androidabschluss.data.model.open.ForecastResponseApi
import de.syntax.androidabschluss.response.ChatRequest
import de.syntax.androidabschluss.response.ChatResponse
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.response.DeeplResponse
import de.syntax.androidabschluss.response.ImageResponse
import de.syntax.androidabschluss.utils.DEEPL_AUTH_KEY
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ApiInterface {

    @POST("chat/completions")
    fun createChatCompletion(
        @Body chatgp : ChatRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "Bearer $OPENAI_API_KEY",


        ): Call<ChatResponse>

    @POST("images/generations")
    fun createImage(
        @Body createImageRequest: CreateImageRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "Bearer $OPENAI_API_KEY",


        ): Call<ImageResponse>

    @POST("/v2/translate")
    fun deepLTranslate(
        @Body deeplRequest: DeeplRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "DeepL-Auth-Key $DEEPL_AUTH_KEY",


        ): Call<DeeplResponse>

    @GET("data/2.5/weather?")
    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units: String,
        @Query("appid") ApiKey: String,
    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast?")
    fun getForecastWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units: String,
        @Query("appid") ApiKey: String,
    ): Call<ForecastResponseApi>

    @Multipart
    @POST("images/edits")
    fun createImageEdit(
        @Part image: MultipartBody.Part,
        @Part mask: MultipartBody.Part,
        @PartMap request: HashMap<String, RequestBody>,
        @Header("Authorization") authorization: String = "Bearer $OPENAI_API_KEY",
    ): Call<ImageResponse>



}