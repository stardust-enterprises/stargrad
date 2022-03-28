object Plugins {
    const val kotlinVersion = "1.6.10"
    const val dokkaVersion = kotlinVersion
    const val nexusPublishVersion = "1.0.0"
    const val ktLintVersion = "10.2.1"
}

object Dependencies {
    const val kotlinVersion = Plugins.kotlinVersion
    val kotlinModules = arrayOf("stdlib")
}
