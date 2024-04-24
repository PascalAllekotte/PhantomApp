package de.syntax.androidabschluss.data.repositorys

import android.app.Application
import android.content.Context
import android.util.Log
import de.syntax.androidabschluss.BuildConfig.OPENAI_API_KEY
import de.syntax.androidabschluss.data.local.ChatGPTDatabase
import de.syntax.androidabschluss.data.local.Resource
import de.syntax.androidabschluss.data.local.getDatabasePicture
import de.syntax.androidabschluss.data.model.open.Chat
import de.syntax.androidabschluss.data.model.open.Data
import de.syntax.androidabschluss.data.model.open.PictureItem
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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

    // Erstellt einen Chat-Vorgang und verarbeitet die Kommunikation
    fun createChatCompletion(message: String, assistantId: String) {
        val receiverId = UUID.randomUUID().toString() // Generiert eine eindeutige ID für den Empfänger
        CoroutineScope(Dispatchers.IO).launch {
            delay(200) // Verzögerung zur Simulation von Netzwerklatenz oder Verarbeitungszeit
            val senderId = UUID.randomUUID().toString() // Generiert eine eindeutige ID für den Sender
            try {
                async {
                    // Fügt die gesendete Nachricht zur Datenbank hinzu
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

                // Erstellt eine Liste der bisherigen Nachrichten für diesen Assistenten und fügt eine Begrüßungsnachricht hinzu, falls nötig
                val messageList = chatGPTDao.getChatListFlow(assistantId).map {
                    it.message
                }.reversed().toMutableList()

                if (messageList.size == 1) {
                    messageList.add(
                        0,
                        Message("Ich bin Pascal dein persönlicher Assistant", "system")
                    )
                }
                async {
                    // Fügt eine leere Nachricht hinzu, um den Platzhalter für die nächste Antwort des Systems zu reservieren
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

                // Erstellt eine Anfrage an den API-Client, um eine Antwort vom ChatGPT Modell zu erhalten
                val chatRequest = ChatRequest(
                    messageList,
                    CHATGPT_MODEL
                )
                apiClient.createChatCompletion(chatRequest).enqueue(object : Callback<ChatResponse> {
                    override fun onResponse(
                        call: Call<ChatResponse>,
                        response: Response<ChatResponse>
                    ) {
                        val code = response.code()
                        if (code == 200) {
                            CoroutineScope(Dispatchers.IO).launch {
                                // Aktualisiert die leere Nachricht mit der echten Antwort, falls vorhanden
                                response.body()?.choices?.get(0)?.message?.let {
                                    chatGPTDao.updateChatPaticularField(
                                        receiverId,
                                        it.content,
                                        it.role,
                                        Date()
                                    )
                                }
                            }
                        } else {
                            // Bei einem Fehler in der API-Antwort, entferne die erstellten Chat-Einträge
                            deleteChatIfApiFailure(receiverId, senderId)
                        }
                    }

                    override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                        // Loggt und behandelt einen Netzwerk- oder Verarbeitungsfehler
                        t.printStackTrace()
                        deleteChatIfApiFailure(receiverId, senderId)
                    }
                })
            } catch (e: Exception) {
                // Fängt und behandelt Ausnahmen während des Chat-Erstellungsprozesses
                e.printStackTrace()
                deleteChatIfApiFailure(receiverId, senderId)
            }
        }
    }



    // Löscht Chateinträge bei einem API-Fehler und informiert den Benutzer
    private fun deleteChatIfApiFailure(receiverId: String, senderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // Führt Löschoperationen für beide Chat-IDs parallel aus
            listOf(
                async { chatGPTDao.deleteChatUsingChatId(receiverId) },
                async { chatGPTDao.deleteChatUsingChatId(senderId) }
            ).awaitAll()
            // Zeigt eine Benachrichtigung auf dem Hauptthread, wenn etwas schiefgeht
            withContext(Dispatchers.Main) {
                application.longToastShow("Irgendwas ist falsch gelaufen")
            }
            // _chatStateFlow.emit(Resource.Error("Irgendwas ist falsch gelaufen"))
        }
    }


    // Initiert den Prozess zur Erstellung eines Bildes über eine externe API
    fun createImage(body: CreateImageRequest, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("bilder", "start")
                // Signalisiert den Start der Bildladung
                _imageStateFlow.emit(Resource.Loading())
                // Sendet die Anfrage, um ein Bild zu erstellen
                apiClient.createImage(
                    body,
                    authorization = "Bearer $OPENAI_API_KEY"
                ).enqueue(object : Callback<ImageResponse> {
                    override fun onResponse(
                        call: Call<ImageResponse>,
                        response: Response<ImageResponse>
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val responseBody = response.body()
                            Log.d("bilder", responseBody.toString())
                            // Verarbeitet die Antwort, wenn das Bild erfolgreich erstellt wurde
                            if (responseBody != null) {
                                imageList.addAll(responseBody.data)
                                val modifiedDataList = ArrayList<Data>().apply {
                                    addAll(imageList)
                                }
                                val imageResponse = ImageResponse(
                                    responseBody.created,
                                    modifiedDataList
                                )
                                // Sendet Erfolgsmeldung mit den Bildinformationen
                                _imageStateFlow.emit(Resource.Success(imageResponse))

                                // Speichert die erhaltenen Bilder in der lokalen Datenbank
                                modifiedDataList.forEach { data ->
                                    val pictureItem = PictureItem(
                                        id = 0,
                                        url = data.url,
                                        created = responseBody.created
                                    )
                                    getDatabasePicture(context).pictureDataBaseDao().insertPicture(pictureItem)
                                }
                            } else {
                                // Sendet eine Erfolgsmeldung ohne Daten, falls die Antwort leer ist
                                _imageStateFlow.emit(Resource.Success(null))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                        // Loggt und behandelt Netzwerk- oder Verarbeitungsfehler
                        t.printStackTrace()
                        CoroutineScope(Dispatchers.IO).launch {
                            _imageStateFlow.emit(Resource.Error(t.message.toString()))
                        }
                    }
                })
            } catch(e: Exception) {
                // Fängt und loggt Ausnahmen während des Bildladeprozesses
                e.printStackTrace()
                _imageStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun createImageEdit(
        prompt: String,
        originalImage: File,
        maskImage: File,
        n: Int,
        size: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Signalisiert den Start des Ladevorgangs
                _imageStateFlow.emit(Resource.Loading())

                // Bereitet die Anfrage vor, indem sie erforderliche Daten in das passende Format konvertiert
                val request = HashMap<String, RequestBody>()
                request["prompt"] = prompt.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                request["n"] = n.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                request["size"] = size.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                // Sendet die Bearbeitungsanfrage an den API-Client
                apiClient.createImageEdit(
                    MultipartBody.Part.createFormData(
                        "image",
                        originalImage.name,
                        originalImage.asRequestBody("image/*".toMediaType())
                    ),
                    MultipartBody.Part.createFormData(
                        "mask",
                        maskImage.name,
                        maskImage.asRequestBody("image/*".toMediaType())
                    ),
                    request
                ).enqueue(object : Callback<ImageResponse> {
                    override fun onResponse(
                        call: Call<ImageResponse>,
                        response: Response<ImageResponse>,
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val responseBody = response.body()
                            Log.d("meet", responseBody.toString())
                            // Verarbeitet die Antwort und aktualisiert den Status bei Erfolg
                            if (responseBody != null) {
                                imageList.addAll(responseBody.data)
                                val modifiedDataList = ArrayList<Data>().apply {
                                    addAll(imageList)
                                }
                                val imageResponse = ImageResponse(
                                    responseBody.created,
                                    modifiedDataList
                                )
                                _imageStateFlow.emit(Resource.Success(imageResponse))

                                // Fügt die bearbeiteten Bilder zur lokalen Speicherung hinzu
                                modifiedDataList.forEach { data ->
                                    val pictureItem = PictureItem(
                                        id = 0,
                                        url = data.url,
                                        created = responseBody.created
                                    )
                                }
                            } else {
                                // Sendet eine Erfolgsmeldung ohne Daten, falls die Antwort leer ist
                                _imageStateFlow.emit(Resource.Success(null))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                        // Loggt und behandelt Netzwerk- oder Verarbeitungsfehler
                        t.printStackTrace()
                        CoroutineScope(Dispatchers.IO).launch {
                            _imageStateFlow.emit(Resource.Error(t.message.toString()))
                        }
                    }
                })
            } catch (e: Exception) {
                // Fängt und loggt Ausnahmen während des Bildbearbeitungsprozesses
                e.printStackTrace()
                _imageStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }


}