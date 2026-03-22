package dam.exer_1_4

import kotlin.math.sqrt

data class Vec2(val x: Double, val y: Double): Comparable<Vec2> {
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Double): Vec2 = Vec2(x * scalar, y * scalar)
    operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

    fun magnitude(): Double = sqrt(x * x + y * y)
    fun dot(other: Vec2): Double = x * other.x + y * other.y
    fun normalized(): Vec2 {
        val mag = magnitude()
        if (mag == 0.0) throw IllegalArgumentException("Cannot normalize a zero vector")
        return Vec2(x / mag, y / mag)
    }

    override fun compareTo(other: Vec2): Int {
        return magnitude().compareTo(other.magnitude())
    }
}