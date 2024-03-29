package dev.pthomain.skeleton.base.utils

import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.*
import org.junit.Assert.assertArrayEquals
import org.mockito.internal.verification.VerificationModeFactory

fun <E> expectException(exceptionType: Class<E>,
                        message: String,
                        action: () -> Unit,
                        checkCause: Boolean = false) {
    try {
        action()
    } catch (e: Exception) {
        val toCheck = if (checkCause) e.cause else e

        if (toCheck != null && exceptionType == toCheck.javaClass) {
            assertEquals("The exception did not have the right message",
                message,
                toCheck.message
            )
            return
        } else {
            fail("Expected exception was not caught: $exceptionType, another one was caught instead $toCheck")
        }
    }

    fail("Expected exception was not caught: $exceptionType")
}

fun assertTrueWithContext(assumption: Boolean,
                          description: String,
                          context: String? = null) =
    assertTrue(withContext(description, context), assumption)

fun assertFalseWithContext(assumption: Boolean,
                           description: String,
                           context: String? = null) =
    assertFalse(withContext(description, context), assumption)

fun <T> assertEqualsWithContext(t1: T,
                                t2: T,
                                description: String,
                                context: String? = null) {
    val withContext = withContext(description, context)

    when {
        t1 is Array<*> && t2 is Array<*> -> assertArrayEquals(withContext, t1, t2)
        t1 is ByteArray && t2 is ByteArray -> assertByteArrayEqualsWithContext(
            t1,
            t2,
            context
        )
        else -> assertEquals(withContext, t1, t2)
    }
}

fun <T> assertNullWithContext(value: T?,
                              description: String,
                              context: String? = null) =
    assertNull(withContext(description, context), value)

fun <T> assertNotNullWithContext(value: T?,
                                 description: String,
                                 context: String? = null) =
    assertNotNull(withContext(description, context), value)

fun failWithContext(description: String,
                    context: String? = null) {
    fail(withContext(description, context))
}

fun withContext(description: String,
                context: String? = null) =
    if (context == null) description
    else "\n$context\n=> $description"

internal fun <T> verifyWithContext(target: T,
                                   context: String?) =
    verify(
        target,
        VerificationModeFactory.description(
            atLeastOnce(),
            "\n$context"
        )
    )

internal fun <T> verifyNeverWithContext(target: T,
                                        context: String?) =
    verify(
        target,
        VerificationModeFactory.description(
            never(),
            "\n$context"
        )
    )

fun assertByteArrayEqualsWithContext(expected: ByteArray?,
                                     other: ByteArray?,
                                     context: String? = null) {
    when {
        expected == null -> assertNullWithContext(
            other,
            "Byte array should be null",
            context
        )
        other != null && other.size == expected.size -> {
            other.forEachIndexed { index, byte ->
                if (expected[index] != byte) {
                    assertEqualsWithContext(
                        expected[index],
                        byte,
                        "Byte didn't match at index $index",
                        context
                    )
                }
            }
        }
        else -> failWithContext(
            "Byte array had the wrong size",
            context
        )
    }
}
