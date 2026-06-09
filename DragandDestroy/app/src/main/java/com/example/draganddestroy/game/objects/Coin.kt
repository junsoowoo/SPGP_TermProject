package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt

class Coin(
    startX: Float,
    startY: Float,
    private val baseValue: Int,
    private val large: Boolean,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val value: Int
        get() = GameStats.getCoinValue(baseValue)

    val radius = if (large) 28f else 17f
    var isDead = false

    private val speed = if (large) 210f else 260f
    private var floatTime = 0f

    private val coinPaint = Paint().apply {
        color = if (large) Color.rgb(255, 180, 30) else Color.YELLOW
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = if (large) 26f else 18f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        floatTime += gctx.frameTime

        x -= speed * gctx.frameTime
        y += kotlin.math.sin(floatTime * 6f) * 20f * gctx.frameTime

        if (x < -50f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.COIN)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, coinPaint)
        canvas.drawText(if (large) "$$" else "$", x, y + 7f, textPaint)
    }

    fun moveToward(targetX: Float, targetY: Float, frameTime: Float) {
        val dx = targetX - x
        val dy = targetY - y
        val length = sqrt(dx * dx + dy * dy)

        if (length <= 0.001f) return

        val magnetSpeed = 720f
        x += dx / length * magnetSpeed * frameTime
        y += dy / length * magnetSpeed * frameTime
    }
}