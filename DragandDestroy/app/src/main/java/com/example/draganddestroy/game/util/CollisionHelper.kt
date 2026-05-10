package com.example.draganddestroy.game.util

object CollisionHelper {

    fun circleCollision(
        x1: Float,
        y1: Float,
        r1: Float,
        x2: Float,
        y2: Float,
        r2: Float,
    ): Boolean {
        val dx = x1 - x2
        val dy = y1 - y2

        val distanceSquared = dx * dx + dy * dy
        val radiusSum = r1 + r2

        return distanceSquared <= radiusSum * radiusSum
    }
}