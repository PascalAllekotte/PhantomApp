import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.Repositorys.OpenAIRepository

class BotViewModel : ViewModel() {
    private val repository = OpenAIRepository()
    val response = MutableLiveData<String>()

    fun getResponse(prompt: String) {
        repository.getResponse(prompt) { result ->
            response.postValue(result)
        }
    }
}
