package fr.stardustenterprises.stargrad

import fr.stardustenterprises.stargrad.dsl.applyIf
import fr.stardustenterprises.stargrad.ext.Extension
import fr.stardustenterprises.stargrad.ext.StargradExtension
import fr.stardustenterprises.stargrad.task.ConfigurableTask
import fr.stardustenterprises.stargrad.task.StargradTask
import fr.stardustenterprises.stargrad.task.Task
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

/**
 * Superclass of any Stargrad plugin.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class StargradPlugin : Plugin<Project> {
    companion object {
        /**
         * The default task group notation.
         */
        internal const val DEFAULT_TASK_GROUP = "NO-GROUP"
    }

    /**
     * The unique identifier of this Gradle plugin.
     */
    abstract val id: String

    /**
     * A set of conflicting plugins.
     */
    protected open val conflictsWith = setOf<String>()

    /**
     * The Gradle [Project] instance to which this plugin applies to.
     */
    protected lateinit var project: Project
        private set

    /**
     * A set of [Runnable]s to execute after the [project] evaluation.
     */
    private val postHooks = mutableListOf<Runnable>()

    /** @inheritDoc */
    final override fun apply(project: Project) {
        this.project = project
        this.applyPlugin()

        project.afterEvaluate {
            this.conflictsWith.filter(project.pluginManager::hasPlugin)
                .ifEmpty {
                    this.postHooks.forEach(Runnable::run)
                    this.afterEvaluate()
                    return@afterEvaluate
                }.also { conflicts ->
                    throw RuntimeException(
                        "Plugin $id conflicts with the following plugins: " +
                            conflicts
                    )
                }
        }
    }

    protected abstract fun applyPlugin()

    /**
     * The method to execute after the [project] gets evaluated.
     */
    protected open fun afterEvaluate() = Unit

    /**
     * Registers the given extension using its
     * [@Extension annotation][Extension].
     *
     * @param extensionClass The class of the extension to register.
     *
     * @return The registered extension.
     */
    protected fun <T : StargradExtension> registerExtension(
        extensionClass: Class<T>,
        vararg objects: Any? = emptyArray(),
    ): T =
        this.project.extensions.create(
            extensionClass.getDeclaredAnnotation(Extension::class.java)
                ?.name
                ?: throw RuntimeException(
                    "Extension class missing @Extension annotation!"
                ),
            extensionClass,
            *objects
        )

    /**
     * Registers the given task using its [@Task annotation][Task].
     *
     * @param taskClass The class of the extension to register.
     *
     * @return The registered task.
     */
    protected fun <T : StargradTask> registerTask(
        taskClass: Class<T>,
    ): TaskProvider<T> {
        val taskAnnotation = taskClass.getDeclaredAnnotation(Task::class.java)
            ?: throw RuntimeException("Task class missing @Task annotation!")

        return this.project.tasks.register(
            taskAnnotation.name,
            taskClass
        ).applyIf(taskAnnotation.group != DEFAULT_TASK_GROUP) {
            this.get().group = taskAnnotation.group
        }
    }

    /**
     * Registers the given configurable task using its
     * [@Task annotation][Task].
     *
     * @param configurableTaskClass The class of the extension to register.
     * @param configureBlock The block that will handle the given task's
     *                       configuration.
     *
     * @return The registered task.
     */
    protected fun <T : ConfigurableTask<*>> registerTask(
        configurableTaskClass: Class<T>,
        configureBlock: T.() -> Unit,
    ): TaskProvider<T> = this.registerTask(configurableTaskClass).also { task ->
        // Enqueue the task's configuration runnable to be run after
        // evaluation:
        this.postHooks.add { task.configure(configureBlock) }
    }

    // - Kotlin extensions, providing a much clearer syntax

    /**
     * @see registerExtension
     */
    protected inline fun <reified T : StargradExtension> registerExtension(
        vararg objects: Any? = emptyArray(),
    ): T = registerExtension(T::class.java, *objects)

    /**
     * @see registerTask
     */
    protected inline fun <reified T : StargradTask>
    registerTask(): TaskProvider<T> = registerTask(T::class.java)

    /**
     * @see registerTask
     */
    protected inline fun <reified T : ConfigurableTask<*>> registerTask(
        noinline configureBlock: T.() -> Unit,
    ) = this.registerTask(T::class.java, configureBlock)
}
