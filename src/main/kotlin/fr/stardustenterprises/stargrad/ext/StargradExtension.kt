@file:Suppress("MemberVisibilityCanBePrivate")

package fr.stardustenterprises.stargrad.ext

import fr.stardustenterprises.stargrad.StargradPlugin
import fr.stardustenterprises.stargrad.task.Task
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import javax.inject.Inject

/**
 * An abstract implementation of the [DefaultTask] which types a Stargrad Task.
 * The [Task] annotation has to be used on top of any implementation of this
 * class.
 *
 * @author lambdagg
 * @since 0.4.0
 */
@Suppress("unused")
abstract class StargradExtension<out T : StargradPlugin>
@Inject constructor(
    /**
     * The Gradle [Project] instance to which this
     * [plugin][StargradPlugin] applies to.
     */
    protected val project: Project,
    /**
     * Instance of the owning [plugin][StargradPlugin].
     */
    protected val plugin: T
) {
    /**
     * The [ObjectFactory] of the current [project].
     */
    @Internal
    protected val objects: ObjectFactory = project.objects
}
