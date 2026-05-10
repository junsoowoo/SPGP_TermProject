package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class PlayerShip(
    private val gctx: GameContext,
) : IGameObject {

    var x = 140f
        private set

    var y = 450f
        private set

    private var targetY = y

    private var fireTimer = 0f
    private val fireInterval = 0.22f
    private val moveSpeed = 1100f

    private val bodyPaint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
    }

    private val wingPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private val gunPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        fireTimer += gctx.frameTime

        targetY = targetY.coerceIn(70f, gctx.metrics.height - 70f)

        val dy = targetY - y
        val moveAmount = moveSpeed * gctx.frameTime

        y += when {
            dy > moveAmount -> moveAmount
            dy < -moveAmount -> -moveAmount
            else -> dy
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, 42f, bodyPaint)
        canvas.drawRect(x - 55f, y - 15f, x + 35f, y + 15f, wingPaint)
        canvas.drawRect(x + 30f, y - 8f, x + 75f, y + 8f, gunPaint)
    }

    fun setTargetY(y: Float) {
        targetY = y
    }

    fun tryFire(): Bullet? {
        if (fireTimer < fireInterval) return null

        fireTimer = 0f

        return Bullet(
            startX = x + 80f,
            startY = y,
            damage = 10,
            color = Color.YELLOW
        )
    }
}