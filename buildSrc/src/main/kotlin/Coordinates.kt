object Coordinates {
    const val name = "stargrad"
    const val desc = "Common library for our gradle plugins."
    const val repoId = "stardust-enterprises/$name"

    const val group = "fr.stardustenterprises"
    const val version = "0.5.2"
}

object Pom {
    val licenses = arrayOf(
        License("ISC License", "https://opensource.org/licenses/ISC"),
    )

    val developers = arrayOf(
        Developer("xtrm"),
        Developer("lambdagg"),
    )
}

data class License(
    val name: String,
    val url: String,
    val distribution: String = "repo",
)

data class Developer(val id: String, val name: String = id)
