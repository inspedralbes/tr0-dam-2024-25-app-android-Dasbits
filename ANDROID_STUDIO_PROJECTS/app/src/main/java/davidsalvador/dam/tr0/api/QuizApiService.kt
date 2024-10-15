package davidsalvador.dam.tr0.api

import davidsalvador.dam.tr0.model.AnswersResponse
import davidsalvador.dam.tr0.model.QuizResponse
import davidsalvador.dam.tr0.model.QuizSubmittedAnswers
import davidsalvador.dam.tr0.model.SubmittedAnswer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Define la interfaz para Retrofit
interface QuizApiService {
    @GET("preguntes")
    fun getPreguntes(): Call<QuizResponse>

    @POST("respostes") //
    fun submitAnswers(@Body answers: QuizSubmittedAnswers): Call<AnswersResponse>
}