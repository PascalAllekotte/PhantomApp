
import de.syntax.androidabschluss.data.model.open.CompletionRequest
import de.syntax.androidabschluss.data.model.open.CompletionResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Header

interface OpenAIApiService {
    @POST("v1/completions")
    @Headers("Content-Type: application/json")
    suspend fun createCompletion(
        @Body requestBody: CompletionRequest,
        @Header("Authorization") authHeader: String
    ): Response<CompletionResponse>
}
