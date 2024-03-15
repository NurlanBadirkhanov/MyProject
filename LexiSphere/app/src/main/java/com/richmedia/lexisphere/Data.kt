import com.google.gson.annotations.SerializedName

data class DefinitionResponse(
    @SerializedName("def") val definitions: List<Definition>
)

data class Definition(
    val text: String,
    val pos: String,
    val tr: List<Translation>
)

data class Translation(
    val text: String,
    val pos: String,
    val syn: List<Synonym>?,
    val mean: List<Mean>?,
    val ex: List<Example>?
)

data class Synonym(
    val text: String
)

data class Mean(
    val text: String
)

data class Example(
    val text: String,
    val tr: List<Translation>
)
