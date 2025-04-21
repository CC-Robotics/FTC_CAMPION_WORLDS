package org.firstinspires.ftc.teamcode.util

import kotlin.math.abs

fun Double.nearTo(other: Double, epsilon: Double): Boolean {
    return abs(this - other) < epsilon
}