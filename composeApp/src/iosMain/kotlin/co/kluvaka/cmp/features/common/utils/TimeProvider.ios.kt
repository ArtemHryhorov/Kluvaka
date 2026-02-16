package co.kluvaka.cmp.features.common.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object TimeProvider {
    actual fun nowMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}
