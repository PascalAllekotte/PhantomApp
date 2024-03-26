package de.syntax.androidabschluss.data.repositorys

 // Angenommen, Message ist unter diesem Pfad.
import android.app.Application
import android.util.Log
import de.syntax.androidabschluss.BuildConfig.OPENAI_API_KEY
import de.syntax.androidabschluss.adapter.local.ChatGPTDatabase
import de.syntax.androidabschluss.adapter.local.Resource
import de.syntax.androidabschluss.data.model.open.Chat
import de.syntax.androidabschluss.data.model.open.Data
import de.syntax.androidabschluss.data.remote.ApiClient
import de.syntax.androidabschluss.response.ChatRequest
import de.syntax.androidabschluss.response.ChatResponse
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.response.ImageResponse
import de.syntax.androidabschluss.response.Message
import de.syntax.androidabschluss.utils.CHATGPT_MODEL
import de.syntax.androidabschluss.utils.longToastShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.UUID

class ChatRepository(val application: Application) {

    private val chatGPTDao = ChatGPTDatabase.getInstance(application).chatGptDao
    private val apiClient = ApiClient.getInstance()

    private val _chatStateFlow = MutableStateFlow<Resource<Flow<List<Chat>>>>(Resource.Loading())
    val chatStateFlow : StateFlow<Resource<Flow<List<Chat>>>>
        get() = _chatStateFlow

    private val _imageStateFlow = MutableStateFlow<Resource<ImageResponse>>(Resource.Success())
    val imageStateFlow : StateFlow<Resource<ImageResponse>>
        get() = _imageStateFlow

    private val imageList = ArrayList<Data>()



    fun getChatList(assistantId: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _chatStateFlow.emit(Resource.Loading())
                val result = async {
                    delay(300)
                    chatGPTDao.getChatList(assistantId)
                }.await()
                _chatStateFlow.emit(Resource.Success(result))
            } catch (e:Exception){
                _chatStateFlow.emit(Resource.Error(e.message.toString()))

            }
        }

    }

    fun createChatCompletion(message: String, assistantId: String) {
        val receiverId = UUID.randomUUID().toString()
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            val senderId = UUID.randomUUID().toString()
            try {
                async {
                    chatGPTDao.insertChat(
                        Chat(
                            senderId,
                            Message(
                                message,
                                "user"
                            ),
                            assistantId,
                            Date()
                        )
                    )
                }.await()

                val messageList = chatGPTDao.getChatListFlow(assistantId).map {
                    it.message
                }.reversed().toMutableList()

                if(messageList.size == 1){
                    messageList.add(
                        0,
                        Message("Ich bin Pascal dein persönlicher Sprachassistant",
                        "system"
                        )
                    )
                }
                async {
                    chatGPTDao.insertChat(
                        Chat(
                            receiverId,
                            Message(
                                "",
                                "Pascal"
                            ),
                            assistantId,
                            Date()
                        )
                    )
                }.await()

                val chatRequest = ChatRequest(
                    messageList,
                    CHATGPT_MODEL
                )
                apiClient.createChatCompletion(chatRequest).enqueue(object :
                    Callback<ChatResponse> {
                    override fun onResponse(
                        call: Call<ChatResponse>,
                        response: Response<ChatResponse>
                    ) {
                        val code = response.code()
                        if (code == 200) {
                            CoroutineScope(Dispatchers.IO).launch {
                                response.body()?.choices?.get(0)?.message?.let {
                                    Log.d("message", it.toString())
                                    chatGPTDao.updateChatPaticularField(
                                        receiverId,
                                        it.content,
                                        it.role,
                                        Date()
                                    )
                                }
                            }
                        } else {
                            Log.d("error", response.errorBody().toString())
                            deleteChatIfApiFailure(receiverId, senderId)
                        }
                    }



                    override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                        t.printStackTrace()
                        deleteChatIfApiFailure(receiverId, senderId)

                    }


                })
            } catch (e: Exception) {
                e.printStackTrace()
                deleteChatIfApiFailure(receiverId, senderId)

            }
        }
    }
    private fun deleteChatIfApiFailure(receiverId: String, senderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            listOf(
                async { chatGPTDao.deleteChatUsingChatId(receiverId) },
                async { chatGPTDao.deleteChatUsingChatId(senderId) }
            ).awaitAll()
            withContext(Dispatchers.Main){
                application.longToastShow("Irgendwas ist falsch gelaufen")
            }
      //      _chatStateFlow.emit(Resource.Error("Irgendwas ist falsch gelaufen"))
        }
    }

    fun createImage(body: CreateImageRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("meet","start")
                _imageStateFlow.emit(Resource.Loading())
                apiClient.createImage(
                    body,
                    authorization = "Bearer $OPENAI_API_KEY"
                ).enqueue(object : Callback<ImageResponse> {
                    override fun onResponse(
                        call: Call<ImageResponse>,
                        response: Response<ImageResponse>,
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val responseBody = response.body()
                            Log.d("meet",responseBody.toString())
                            if (responseBody != null){
                                imageList.addAll(responseBody.data)
                                val modifiedDataList = ArrayList<Data>().apply {
                                    addAll(imageList)
                                }
                                val imageResponse = ImageResponse(
                                    responseBody.created,
                                    modifiedDataList
                                )
                                _imageStateFlow.emit(Resource.Success(imageResponse))
                            } else{
                                _imageStateFlow.emit(Resource.Success(null))


                            }
                        }
                    }

                    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                        t.printStackTrace()
                        CoroutineScope(Dispatchers.IO).launch {
                            _imageStateFlow.emit(Resource.Error(t.message.toString()))
                        }
                    }
                })
            } catch(e: Exception) {
                e.printStackTrace()
                _imageStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }


}