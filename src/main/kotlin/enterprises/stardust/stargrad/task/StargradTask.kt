package enterprises.stardust.stargrad.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * An abstract implementation of the [DefaultTask] which types a Stargrad Task.
 * The [Task] annotation has to be used on top of any implementation of this
 * class.
 *
 * @author xtrm
 * @since 0.1.0
 */
abstract class StargradTask : DefaultTask() {
    /**
     * The method to run when this task has to.
     */
    @TaskAction abstract fun run()
}
