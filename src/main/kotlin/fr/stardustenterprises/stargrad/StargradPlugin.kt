package fr.stardustenterprises.stargrad

import fr.stardustenterprises.stargrad.ext.Extension
import fr.stardustenterprises.stargrad.task.ConfigurableTask
import fr.stardustenterprises.stargrad.task.StargradTask
import fr.stardustenterprises.stargrad.task.Task
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

abstract class StargradPlugin : Plugin<Project> {
    protected lateinit var project: Project
        private set

    abstract val pluginId: String

    private val postHooks = mutableListOf<Runnable>()

    open fun applyPlugin() = Unit
    open fun afterEvaluate(project: Project) = Unit

    override fun apply(target: Project) {
        this.project = target

        applyPlugin()

        this.project.afterEvaluate {
            conflictsWithPlugins().firstOrNull(this.project.pluginManager::hasPlugin) ?: it.run {
                postHooks.forEach(Runnable::run)

                afterEvaluate(this)
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

    protected fun <T : StargradTask> task(taskClass: Class<out T>): TaskProvider<out T> {
        val taskAnnotation = taskClass.getDeclaredAnnotation(Task::class.java)
            ?: throw RuntimeException("Task class missing @Task annotation!")
        return this.project.tasks.register(taskAnnotation.name, taskClass).also {
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
            configureBlock.invoke(task.get())
        }
        return task
    }

    open fun conflictsWithPlugins(): Array<String> = arrayOf()
}
