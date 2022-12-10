package enterprises.stardust.stargrad.ext

/**
 * Used to annotate extensions, giving them a name.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extension(
    /**
     * This extension's name.
     */
    val name: String,
)
