package co.kluvaka.cmp.features.common.utils

import kotlinx.datetime.Clock

actual object TimeProvider {
    actual fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()
}
