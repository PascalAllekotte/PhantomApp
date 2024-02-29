import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import de.syntax.androidabschluss.BuildConfig
import de.syntax.androidabschluss.data.model.open.CompletionRequest


class OpenAIRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiService: OpenAIApiService = retrofit.create(OpenAIApiService::class.java)
    private val token = "Bearer ${BuildConfig.OPENAI_API_KEY}"

    suspend fun createCompletion(prompt: String): Result<String> {
        val request = CompletionRequest(prompt, 50) // Beispiel: max_tokens auf 50 gesetzt
        return try {
            val response = apiService.createCompletion(request, token)
            if (response.isSuccessful) {
                val textResponse = response.body()?.choices?.firstOrNull()?.text ?: ""
                Result.success(textResponse)
            } else {
                Result.failure(Exception("API call failed with response code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
