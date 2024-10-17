package davidsalvador.dam.tr0.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import davidsalvador.dam.tr0.R
import davidsalvador.dam.tr0.api.QuizApiService
import davidsalvador.dam.tr0.api.RetrofitClient
import davidsalvador.dam.tr0.model.Pregunta
import davidsalvador.dam.tr0.model.QuizResponse
import davidsalvador.dam.tr0.model.SubmittedAnswer

// imports per peticions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuizActivity : AppCompatActivity() {

    private var userName = ""

    private var timestampInicial: Long = 0L

    private var currentIndex = 0
    private var preguntes: List<Pregunta> = emptyList()
    private var submittedAnswers: MutableList<SubmittedAnswer?> = MutableList(10) { null }

    private lateinit var currentImage: ImageView
    private lateinit var currentQuestionView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var buttonAnswer1: Button
    private lateinit var buttonAnswer2: Button
    private lateinit var buttonAnswer3: Button
    private lateinit var buttonAnswer4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Inicializa las vistas aquí
        currentImage = findViewById(R.id.currentImageView)
        currentQuestionView = findViewById(R.id.currentQuestion)
        questionTextView = findViewById(R.id.question)
        buttonAnswer1 = findViewById(R.id.ans_1)
        buttonAnswer2 = findViewById(R.id.ans_2)
        buttonAnswer3 = findViewById(R.id.ans_3)
        buttonAnswer4 = findViewById(R.id.ans_4)

        // Recibe el nombre del usuario pasado desde MainActivity
        userName = intent.getStringExtra("USER_NAME").toString()

        // Encuentra el TextView y muestra el nombre del usuario
        val userNameTextView: TextView = findViewById(R.id.userNameTextView)
        userNameTextView.text = "Quiz de $userName"

        fetchPreguntes()
        timestampInicial = System.currentTimeMillis()
        println("Timestamp inicial: $timestampInicial")

        buttonAnswer1.setOnClickListener { selectAnswer(1) }
        buttonAnswer2.setOnClickListener { selectAnswer(2) }
        buttonAnswer3.setOnClickListener { selectAnswer(3) }
        buttonAnswer4.setOnClickListener { selectAnswer(4) }


        val buttonSubmit: Button = findViewById(R.id.submit)
        buttonSubmit.setOnClickListener{submit()}
    }

    private fun fetchPreguntes(){
        // Crea una instancia de Retrofit y del servicio de API
        val quizApiService = RetrofitClient.createQuizApiService()

        // Llamada asíncrona
        quizApiService.getPreguntes().enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                if (response.isSuccessful) {
                    val preguntasRecibidas = response.body()?.preguntes
                    if (preguntasRecibidas != null) {
                        // Aquí asignamos el valor recibido a la variable preguntes
                        preguntes = preguntasRecibidas
                        Log.d("QuizActivity", "Preguntas recibidas: $preguntes")

                        actualizarUI()
                    }
                } else {
                    Log.e("QuizActivity", "Error en la respuesta: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                Log.e("QuizActivity", "Fallo en la llamada: ${t.message}")
            }
        })
    }

    private fun selectAnswer(answerId: Int) {
        // Verifica que el índice actual esté dentro de los límites
        if (currentIndex in 0 until submittedAnswers.size) {
            // Crea un nuevo SubmittedAnswer con el questionId y selectedAnswer
            val newAnswer = SubmittedAnswer(questionId = preguntes[currentIndex].id, selectedAnswer = answerId)

            // Almacena la respuesta en el índice actual
            submittedAnswers[currentIndex] = newAnswer

            Log.d("QuizActivity", "Respuesta seleccionada para la pregunta $answerId: ${newAnswer.selectedAnswer}")
        } else {
            Log.e("QuizActivity", "Índice actual fuera de los límites: $currentIndex")
        }
    }

    private fun submit() {
        if (submittedAnswers[currentIndex] != null) {
            if (currentIndex == 9) {

                val gson = Gson()
                var submittedAnswersJson = gson.toJson(submittedAnswers)

                val timestampFinal = System.currentTimeMillis()
                val tempsMilisegs = timestampFinal - timestampInicial

                val intent = Intent(this, ResultsActivity::class.java)

                intent.putExtra("USER_NAME", userName)
                intent.putExtra("SUBMITTED_ANSWERS",submittedAnswersJson)
                intent.putExtra("TEMPS_QUIZ", tempsMilisegs.toString())

                startActivity(intent)
            } else {
                currentIndex++
                actualizarUI()
            }
        } else {
            Toast.makeText(this, "Selecciona una respuesta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarUI() {
        Picasso.get().load(preguntes[currentIndex].imatge).into(currentImage)
        currentQuestionView.text = "Pregunta ${currentIndex+1}/10"
        questionTextView.text = preguntes[currentIndex].pregunta
        buttonAnswer1.text = preguntes[currentIndex].respostes[0].etiqueta
        buttonAnswer2.text = preguntes[currentIndex].respostes[1].etiqueta
        buttonAnswer3.text = preguntes[currentIndex].respostes[2].etiqueta
        buttonAnswer4.text = preguntes[currentIndex].respostes[3].etiqueta
    }
}
