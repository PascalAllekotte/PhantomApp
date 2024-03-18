package de.syntax.androidabschluss.data.repositorys

 // Angenommen, Message ist unter diesem Pfad.
import android.util.Log
import de.syntax.androidabschluss.data.remote.ApiClient
import de.syntax.androidabschluss.response.ChatRequest
import de.syntax.androidabschluss.response.ChatResponse
import de.syntax.androidabschluss.response.Message
import de.syntax.androidabschluss.utils.CHATGPT_MODEL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {

    private val apiClient = ApiClient.getInstance()
    fun createChatCompletion(message: String){

            try {
                val chatRequest = ChatRequest(
                    arrayListOf(
                        Message(
                            "bitte beantworte nur mathe fragen alles andere beneinst du freundlich",
                            "system"
                        ),
                        Message(
                            message,
                            "user"
                    )
                    ),
                    CHATGPT_MODEL
                )
                apiClient.createChatCompletion(chatRequest).enqueue(object :
                    Callback<ChatResponse> {
                    override fun onResponse(
                        call: Call<ChatResponse>,
                        response: Response<ChatResponse>
                    ) {
                        val code = response.code()
                        if (code == 200){
                            response.body()?.choices?.get(0)?.message?.let {
                                Log.d("message", it.toString())
                            }
                        }else{
                            Log.d("error", response.errorBody().toString())
                        }
                    }

                    override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    t.printStackTrace()
                    }


                })
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

}