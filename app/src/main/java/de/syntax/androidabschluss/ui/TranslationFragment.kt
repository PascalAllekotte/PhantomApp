package com.example.random.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.syntax.androidabschluss.databinding.FragmentTranslationBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TranslationFragment : Fragment() {

    private lateinit var binding: FragmentTranslationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationBinding.inflate(inflater, container, false)

      //  binding.buttonTranslate.setOnClickListener {
//        }

        return binding.root
    }

    private fun translateText(text: String) {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = "{\"text\":\"$text\",\"target_lang\":\"DE\"}"
            .toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://api-free.deepl.com/v2/translate")
            .post(requestBody)
            .addHeader("Authorization", "DeepL-Auth-Key 48329d96-cd7d-4bc4-ac7f-f9d0e666f2f9:fx")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Error handling here
                activity?.runOnUiThread {
             //       binding.textViewResult.text = "Error:safdjlsfvd $e"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // Error handling here
                    activity?.runOnUiThread {
                    //    binding.textViewResult.text = "Error:sfDA ${response.message}"
                    }
                    return
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrBlank()) {
                    // Error handling here
                    activity?.runOnUiThread {
              //          binding.textViewResult.text = "Error: Empty response body"
                    }
                    return
                }

                val jsonObject = JsonParser.parseString(responseBody).asJsonObject
                val translations = jsonObject["translations"].asJsonArray
                val translatedText = if (translations.size() > 0) {
                    translations[0].asJsonObject["text"].asString
                } else {
                    "Translation not available"
                }

                activity?.runOnUiThread {
          //          binding.textViewResult.text = translatedText
                }
            }
        })
    }
}
