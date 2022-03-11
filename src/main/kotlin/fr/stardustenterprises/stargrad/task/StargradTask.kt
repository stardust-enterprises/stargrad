package fr.stardustenterprises.stargrad.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class StargradTask : DefaultTask() {
    @TaskAction
    abstract fun doTask()
}
