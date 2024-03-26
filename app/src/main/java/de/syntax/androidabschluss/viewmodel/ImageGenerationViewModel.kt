package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
/**
class ImageGenerationViewModel : ViewModel() {


    val chatGPT_API_KEY = "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"
    private val client = OkHttpClient()
    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: LiveData<String?> = _imageUrl

    private val _isInProgress = MutableLiveData<Boolean>()
    val isInProgress: LiveData<Boolean> = _isInProgress

    fun generateImage(prompt: String) {
        _isInProgress.value = true
        val jsonBody = JSONObject().apply {
            put("prompt", prompt)
            put("size", "1024x1024")
            put("style", "natural")
            put("quality", "hd")



        }
        val requestBody: RequestBody = RequestBody.create(JSON, jsonBody.toString())
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .header("Authorization", "Bearer $chatGPT_API_KEY")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _isInProgress.postValue(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body!!.string())
                    val imgUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url")
                    _imageUrl.postValue(imgUrl)
                } else {
                    _imageUrl.postValue(null)
                }
                _isInProgress.postValue(false)
            }
        })
    }

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }
}
**/