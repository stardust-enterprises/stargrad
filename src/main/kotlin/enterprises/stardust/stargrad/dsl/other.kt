@file:Suppress("unused")

package enterprises.stardust.stargrad.dsl

/**
 * Applies the given [block] if the [if] value is true, only return the
 * object itself if not.
 *
 * @param if Whether the block should be applied.
 * @param block The block to be applied.
 *
 * @return This instance.
 */
inline fun <T> T.applyIf(`if`: Boolean, block: T.() -> Unit): T =
    apply { if (`if`) { this.apply(block) } }

/**
 * Applies the given [block] if the [ifPredicate] results to true, only return
 * the object itself if not.
 *
 * @param ifPredicate Results to whether the block should be applied.
 * @param block The block to be applied.
 *
 * @return This instance.
 */
inline fun <T> T.applyIf(
    ifPredicate: T.() -> Boolean,
    block: T.() -> Unit
): T = applyIf(ifPredicate(this), block)
