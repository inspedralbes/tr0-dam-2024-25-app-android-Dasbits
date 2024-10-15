package davidsalvador.dam.tr0.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL base del servidor de la API
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Crear instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Función para crear el servicio de API
    fun createQuizApiService(): QuizApiService {
        return retrofit.create(QuizApiService::class.java)
    }
}