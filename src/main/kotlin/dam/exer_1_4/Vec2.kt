package dam.exer_1_4

import kotlin.math.sqrt

data class Vec2(val x: Double, val y: Double): Comparable<Vec2> {
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Double): Vec2 = Vec2(x * scalar, y * scalar)
    operator fun unaryMinus(): Vec2 = Vec2(-x, -y)


}