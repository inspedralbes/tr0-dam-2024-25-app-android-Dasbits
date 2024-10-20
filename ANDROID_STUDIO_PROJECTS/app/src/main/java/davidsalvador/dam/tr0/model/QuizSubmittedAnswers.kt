package davidsalvador.dam.tr0.model

data class QuizSubmittedAnswers(
    var userName: String,
    var answers: List<SubmittedAnswer>,
    var temps: Int?
)

data class SubmittedAnswer(
    val questionId: Int,
    var selectedAnswer: Int
)
