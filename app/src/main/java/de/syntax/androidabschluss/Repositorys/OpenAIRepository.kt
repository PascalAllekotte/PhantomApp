package de.syntax.androidabschluss.Repositorys

import okhttp3.*
import java.io.IOException

class OpenAIRepository {

    private val client = OkHttpClient()

    fun getResponse(prompt: String, callback: (String) -> Unit) {
        val requestBody = FormBody.Builder()
            .add("prompt", prompt)
            .add("max_tokens", "150")
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/davinci/completions")
            .post(requestBody)
            .addHeader("Authorization", "asst_AXSYZzogJqB5ht6mnehagtOd")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseString = response.body?.string()
                    callback(responseString ?: "")
                }
            }
        })
    }
}
