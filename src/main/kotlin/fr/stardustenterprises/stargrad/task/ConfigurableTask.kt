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
abstract class ConfigurableTask<E : StargradExtension> : StargradTask() {
    private var _configuration: E? = null

    /**
     * The [StargradExtension] corresponding to this task's configuration.
     */
    @get:Internal
    protected val configuration: E
        get() = _configuration!!

    /**
     * The method to execute when applying a new [configuration] value.
     */
    protected open fun applyConfiguration() = Unit

    /**
     * Configure this task using the given [configuration].
     *
     * @param configuration The new configuration.
     *
     * @return This instance.
     */
    fun configure(configuration: E) = apply {
        this._configuration = configuration
        this.applyConfiguration()
    }
}
