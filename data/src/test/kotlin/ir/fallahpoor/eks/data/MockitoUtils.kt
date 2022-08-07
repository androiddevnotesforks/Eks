package ir.fallahpoor.eks.data

import org.mockito.Mockito
import kotlin.reflect.KClass

/**
 *
 * Mockito returns null values for calls to method like any(), which can cause IllegalStateException when
 * passing them to non-nullable parameters. The following code tries to create actual instances to return.
 *
 * Code is copied from Mockito Kotlin.
 * https://github.com/mockito/mockito-kotlin
 */

inline fun <reified T : Any> any(): T =
    Mockito.any(T::class.java) ?: createInstance()

inline fun <reified T : Any> createInstance(): T = when (T::class) {
    Boolean::class -> false as T
    Byte::class -> 0.toByte() as T
    Char::class -> 0.toChar() as T
    Short::class -> 0.toShort() as T
    Int::class -> 0 as T
    Long::class -> 0L as T
    Float::class -> 0f as T
    Double::class -> 0.0 as T
    else -> createInstance(T::class)
}

fun <T : Any> createInstance(kClass: KClass<T>): T = castNull()

@Suppress("UNCHECKED_CAST")
private fun <T> castNull(): T = null as T