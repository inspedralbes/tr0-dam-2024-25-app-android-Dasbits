package davidsalvador.dam.tr0.model

data class QuizResponse(
    val preguntes: List<Pregunta>
)

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val respostes: List<Resposta>,
    val imatge: String
)

data class Resposta(
    val id: Int,
    val etiqueta: String
)