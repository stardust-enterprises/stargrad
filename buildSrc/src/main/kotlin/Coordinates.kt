object Coordinates {
    const val NAME = "stargrad"
    const val DESC = "Common library for our gradle plugins."
    const val REPO_ID = "stardust-enterprises/$NAME"

    const val GROUP = "fr.stardustenterprises"
    const val VERSION = "0.1.0"
}

object Pom {
    val licenses = arrayOf(
        License("ISC License", "https://opensource.org/licenses/ISC")
    )
    val developers = arrayOf(
        Developer("xtrm")
    )
}

data class License(val name: String, val url: String, val distribution: String = "repo")
data class Developer(val id: String, val name: String = id)
