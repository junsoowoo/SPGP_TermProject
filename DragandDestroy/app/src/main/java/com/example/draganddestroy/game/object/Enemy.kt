package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Enemy(
    startX: Float,
    startY: Float,
    private var hp: Int,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 45f

    var isDead = false
        private set

    private val speed = 170f
    private val maxHp = hp

    private val bodyPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val hpBgPaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 8f
    }

    private val hpPaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
    }

    override fun update(gctx: GameContext) {
        x -= speed * gctx.frameTime

        if (x < -100f) {
            isDead = true

            val scene = gctx.scene as? com.example.draganddestroy.game.scene.MainScene
            scene?.world?.remove(this, com.example.draganddestroy.game.scene.MainScene.Layer.ENEMY)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, bodyPaint)

        canvas.drawText(hp.toString(), x, y + 10f, textPaint)

        val hpRatio = hp.toFloat() / maxHp.toFloat()
        val left = x - 45f
        val right = x + 45f
        val top = y - 60f

        canvas.drawLine(left, top, right, top, hpBgPaint)
        canvas.drawLine(left, top, left + 90f * hpRatio.coerceIn(0f, 1f), top, hpPaint)
    }

    fun takeDamage(damage: Int) {
        hp -= damage

        if (hp <= 0) {
            hp = 0
            isDead = true
        }
    }
}