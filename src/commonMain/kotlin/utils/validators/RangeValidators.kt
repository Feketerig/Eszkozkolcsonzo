package utils.validators

import kotlin.math.max
import kotlin.math.min

fun rangesOverlap(start1: Long, end1: Long, start2: Long, end2: Long) = max(start1, start2) <= min(end1, end2)