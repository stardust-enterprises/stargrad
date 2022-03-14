package fr.stardustenterprises.stargrad.task

/**
 * Used to annotate standalone tasks.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Task(
    val group: String = "NO-GROUP",
    val name: String
)
