package fr.stardustenterprises.stargrad.task

import fr.stardustenterprises.stargrad.DEFAULT_TASK_GROUP

/**
 * Used to annotate standalone tasks, giving them a name and a group.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Task(
    val name: String,
    val group: String = DEFAULT_TASK_GROUP,
)
