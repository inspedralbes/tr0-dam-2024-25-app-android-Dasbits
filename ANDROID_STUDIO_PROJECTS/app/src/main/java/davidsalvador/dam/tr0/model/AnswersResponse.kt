package davidsalvador.dam.tr0.model

data class AnswersResponse (
    val questionsInfo: List<questionInfo>
)

data class questionInfo(
    val questionId: Int,
    val correcta: Boolean
)