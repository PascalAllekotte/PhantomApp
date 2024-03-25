package de.syntax.androidabschluss.data.remote

import de.syntax.androidabschluss.response.ChatRequest
import de.syntax.androidabschluss.response.ChatResponse
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.response.ImageResponse
import de.syntax.androidabschluss.utils.OPENAI_API_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("chat/completions")
    fun createChatCompletion(
        @Body chatgp : ChatRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "Bearer $OPENAI_API_KEY",


        ): Call<ChatResponse>

    @POST("image/generations")
    fun createImage(
        @Body createImageRequest: CreateImageRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "Bearer $OPENAI_API_KEY",


        ): Call<ImageResponse>




}