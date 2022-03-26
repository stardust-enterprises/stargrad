package fr.stardustenterprises.stargrad.task

import fr.stardustenterprises.stargrad.ext.StargradExtension
import org.gradle.api.tasks.Internal

/**
 * An abstract implementation of the [StargradTask] which adds a configuration
 * layer.
 *
 * @author xtrm
 * @since 0.1.0
 */
@Suppress("unused")
abstract class ConfigurableTask<T : StargradExtension> : StargradTask() {
    private var _configuration: T? = null

    @get:Internal
    protected val configuration: T
        get() = _configuration!!

    abstract fun applyConfiguration()

    fun configure(configuration: T) {
        this._configuration = configuration
        applyConfiguration()
    }
}
