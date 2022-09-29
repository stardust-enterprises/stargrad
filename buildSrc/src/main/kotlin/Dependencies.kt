object Plugins {
    const val kotlinVersion = "1.7.20"
    const val dokkaVersion = "1.7.10"
    const val nexusPublishVersion = "1.0.0"
    const val ktLintVersion = "11.0.0"
}

object Dependencies {
    const val kotlinVersion = Plugins.kotlinVersion
    val kotlinModules = arrayOf("stdlib")
}
