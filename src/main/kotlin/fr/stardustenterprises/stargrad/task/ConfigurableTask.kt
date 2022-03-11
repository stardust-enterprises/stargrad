package fr.stardustenterprises.stargrad.task

import org.gradle.api.tasks.Internal

abstract class ConfigurableTask<in T> : StargradTask() {

    private var _configuration: @UnsafeVariance T? = null

    @get:Internal
    protected val configuration: @UnsafeVariance T
        get() = _configuration!!

    open fun applyConfiguration() = Unit

    fun configure(configuration: T) {
        this._configuration = configuration
        applyConfiguration()
    }
}
