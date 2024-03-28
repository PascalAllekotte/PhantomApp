package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import de.syntax.androidabschluss.adapter.Request.DeeplRequest
import de.syntax.androidabschluss.data.remote.ApiClientDeepL
import de.syntax.androidabschluss.databinding.FragmentDeepLBinding
import de.syntax.androidabschluss.response.DeeplResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeepLFragment : Fragment() {

    private lateinit var binding: FragmentDeepLBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeepLBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btntranslate.setOnClickListener {
            translateText()
        }
    }

    private fun translateText() {
        val textToTranslate = binding.textToTranslate.text.toString()
        if (textToTranslate.isNotEmpty()) {
            val apiInterface = ApiClientDeepL.getInstance()
            val request = DeeplRequest(target_lang = "DE", text = listOf(textToTranslate))
            apiInterface.deepLTranslate(request).enqueue(object : Callback<DeeplResponse> {
                override fun onResponse(call: Call<DeeplResponse>, response: Response<DeeplResponse>) {
                    if (response.isSuccessful) {
                        val translation = response.body()?.translations?.firstOrNull()?.text ?: ""
                        binding.translatedtext.setText(translation)
                    } else {
                        // Fehlerbehandlung
                        Toast.makeText(context, "Ein Fehler ist aufgetreten", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeeplResponse>, t: Throwable) {
                    // Netzwerkfehlerbehandlung
                    Toast.makeText(context, "Netzwerkfehler: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

