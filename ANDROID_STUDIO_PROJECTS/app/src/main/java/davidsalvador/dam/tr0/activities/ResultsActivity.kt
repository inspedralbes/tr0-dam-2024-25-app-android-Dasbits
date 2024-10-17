package davidsalvador.dam.tr0.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import davidsalvador.dam.tr0.R
import davidsalvador.dam.tr0.api.RetrofitClient
import davidsalvador.dam.tr0.model.AnswersResponse
import davidsalvador.dam.tr0.model.QuizResponse
import davidsalvador.dam.tr0.model.QuizSubmittedAnswers
import davidsalvador.dam.tr0.model.SubmittedAnswer
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class ResultsActivity : AppCompatActivity() {
    private lateinit var resultsView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resultsView = findViewById(R.id.results)

        // Recibir el nombre de usuario
        val userName = intent.getStringExtra("USER_NAME")

        // Recibir la lista de respuestas enviadas
        val submittedAnswers = intent.getStringExtra("SUBMITTED_ANSWERS")

        // Recibir el tiempo del quiz
        val tempsMiliseg = intent.getStringExtra("TEMPS_QUIZ")

        val tempsQuiz = tempsMiliseg?.toInt()

        val gson = Gson()

        val ansListType = object : TypeToken<List<SubmittedAnswer>>() {}.type
        val quizSubmittedAnswers: List<SubmittedAnswer> = gson.fromJson(submittedAnswers, ansListType)

        val QuizSubmittedAnswers = QuizSubmittedAnswers(userName = userName.toString(),answers = quizSubmittedAnswers, temps = tempsQuiz)

        submitAnswers(QuizSubmittedAnswers)

        var buttonReiniciar: Button = findViewById(R.id.retry)

        buttonReiniciar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun submitAnswers(quizSubmittedAnswers: QuizSubmittedAnswers) {

        val quizApiService = RetrofitClient.createQuizApiService()
        val call = quizApiService.submitAnswers(quizSubmittedAnswers)

        call.enqueue(object : Callback<AnswersResponse> {
            override fun onResponse(call: Call<AnswersResponse>, response: Response<AnswersResponse>) {
                if (response.isSuccessful) {
                    // Manejar la respuesta exitosa
                    val quizResponse = response.body()?.questionsInfo
                    if(quizResponse != null){
                        var puntuacio = 0
                        for(answer in quizResponse){
                            if(answer.correcta) puntuacio++
                        }
                        resultsView.text = "${puntuacio}/10 correctes"
                    }
                    println("Respuestas enviadas exitosamente: $quizResponse")
                } else {
                    // Manejar el error de respuesta
                    println("Error en la respuesta: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AnswersResponse>, t: Throwable) {
                // Manejar el error de la llamada
                println("Error en la llamada: ${t.message}")
            }
        })
    }

}