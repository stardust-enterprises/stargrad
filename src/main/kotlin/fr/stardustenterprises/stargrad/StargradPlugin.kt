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

const val DEFAULT_TASK_GROUP = "NO-GROUP"

/**
 * Superclass of any Stargrad plugin.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Suppress("unused")
abstract class StargradPlugin : Plugin<Project> {
    protected lateinit var project: Project
        private set

    abstract val pluginId: String

    private val postHooks: MutableList<Runnable> =
        mutableListOf()

    final override fun apply(project: Project) {
        this.project = project
        this.applyPlugin()

        project.afterEvaluate {
            this.conflictsWithPlugins().filter(
                project.pluginManager::hasPlugin
            ).ifEmpty {
                this.postHooks.forEach(Runnable::run)
                this.afterEvaluate()
                return@afterEvaluate
            }.also { conflicts ->
                throw RuntimeException(
                    "Plugin $pluginId conflicts with the following plugins: " +
                        conflicts
                )
            }
        }
    }

    protected abstract fun applyPlugin()

    protected open fun afterEvaluate() = Unit

    protected fun <T : StargradExtension> registerExtension(
        extensionClass: Class<T>,
    ): T =
        this.project.extensions.create(
            extensionClass.getDeclaredAnnotation(Extension::class.java)
                ?.name
                ?: throw RuntimeException(
                    "Extension class missing @Extension annotation!"
                ),
            extensionClass
        )

    protected fun <T : StargradTask> registerTask(
        taskClass: Class<out T>,
    ): TaskProvider<out T> {
        val taskAnnotation = taskClass.getDeclaredAnnotation(Task::class.java)
            ?: throw RuntimeException("Task class missing @Task annotation!")

        return this.project.tasks.register(
            taskAnnotation.name,
            taskClass
        ).applyIf(taskAnnotation.group != DEFAULT_TASK_GROUP) {
            this.get().group = taskAnnotation.group
        }
    }

    protected fun <T : ConfigurableTask<*>> registerTask(
        configurableTask: Class<out T>,
        configureBlock: T.() -> Unit,
    ): TaskProvider<out T> {
        val task = this.registerTask(configurableTask)
        this.postHooks.add {
            task.configure(configureBlock)
        }
        return task
    }

    protected open fun conflictsWithPlugins() = arrayOf<String>()

    // - Kotlin extensions, providing a much clearer syntax

    protected inline fun <reified T : StargradExtension>
        registerExtension(): T = registerExtension(T::class.java)

    protected inline fun <reified T : StargradTask>
        registerTask(): TaskProvider<out T> = registerTask(T::class.java)

    protected inline fun <reified T : ConfigurableTask<*>> registerTask(
        noinline configureBlock: T.() -> Unit
    ) = this.registerTask(T::class.java, configureBlock)
}
