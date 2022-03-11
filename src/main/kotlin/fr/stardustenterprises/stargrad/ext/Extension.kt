package fr.stardustenterprises.stargrad.ext

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extension(
    val name: String
)
