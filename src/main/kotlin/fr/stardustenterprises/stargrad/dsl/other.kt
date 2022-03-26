@file:Suppress("unused")

package fr.stardustenterprises.stargrad.dsl

inline fun <T> T.applyIf(`if`: Boolean, block: T.() -> Unit): T =
    apply { if (`if`) { this.apply(block) } }

inline fun <T> T.applyIf(
    ifPredicate: T.() -> Boolean,
    block: T.() -> Unit
): T = applyIf(ifPredicate(this), block)
