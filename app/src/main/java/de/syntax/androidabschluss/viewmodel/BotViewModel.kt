import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BotViewModel : ViewModel() {
    val chatMessages = MutableLiveData<MutableList<String>>()

    init {
        chatMessages.value = mutableListOf()
    }

    fun getCompletion(userInput: String) {
        // Hier rufen Sie normalerweise Ihre Repository-Methode auf, um die Antwort von OpenAI zu erhalten.
        // Zum Vereinfachen fügen wir nur die Benutzereingabe und eine simulierte Antwort direkt hinzu.
        val updatedMessages = chatMessages.value ?: mutableListOf()
        updatedMessages.add("You: $userInput")
        updatedMessages.add("Bot: Antwort auf '$userInput'")
        chatMessages.value = updatedMessages
    }
}

