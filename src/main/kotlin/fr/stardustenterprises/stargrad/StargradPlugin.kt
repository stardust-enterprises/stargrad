package fr.stardustenterprises.stargrad

import fr.stardustenterprises.stargrad.ext.Extension
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
abstract class StargradPlugin : Plugin<Project> {
    protected lateinit var project: Project
        private set

    abstract val pluginId: String

    private val postHooks: MutableList<Runnable> =
        mutableListOf()

    open fun applyPlugin() = Unit

    open fun afterEvaluate(project: Project) = Unit

    final override fun apply(target: Project) {
        this.project = target
        applyPlugin()

        this.project.afterEvaluate { proj ->
            conflictsWithPlugins().firstOrNull(
                this.project.pluginManager::hasPlugin
            ) ?: proj.run {
                postHooks.forEach(Runnable::run)

                afterEvaluate(proj)
                return@afterEvaluate
            }
            val present = conflictsWithPlugins().filter(project.pluginManager::hasPlugin).toList()
            throw RuntimeException("Plugin $pluginId conflicts with the following plugins: $present")
        }
    }

    protected fun <T> extension(extensionClass: Class<T>): T {
        val extensionAnnotation = extensionClass.getDeclaredAnnotation(Extension::class.java)
            ?: throw RuntimeException(
                "Extension class missing @Extension annotation!"
            )

        return this.project.extensions.create(extensionAnnotation.name, extensionClass)
    }

    protected fun <T : StargradTask> task(
        taskClass: Class<out T>,
    ): TaskProvider<out T> {
        val taskAnnotation = taskClass.getDeclaredAnnotation(Task::class.java)
            ?: throw RuntimeException("Task class missing @Task annotation!")

        return this.project.tasks.register(
            taskAnnotation.name,
            taskClass
        ).also {
            if (taskAnnotation.group != "NO-GROUP") {
                it.get().group = taskAnnotation.group
            }
        }
    }

    protected fun <T, C : ConfigurableTask<T>> task(
        configurableTask: Class<out C>,
        configureBlock: C.() -> Unit,
    ): TaskProvider<out C> {
        val task = this.task(configurableTask)
        this.postHooks.add {
            task.configure(configureBlock)
        }
        return task
    }

    open fun conflictsWithPlugins(): Array<String> = arrayOf()
}
