package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt

class Bullet(
    startX: Float,
    startY: Float,
    val damage: Int,
    private val color: Int = Color.YELLOW,
    dirX: Float = 1f,
    dirY: Float = 0f,
    private val speed: Float = 1300f,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 10f
    var isDead = false

    private var vx = 1f
    private var vy = 0f

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    init {
        setDirection(dirX, dirY)
    }

    override fun update(gctx: GameContext) {
        x += vx * speed * gctx.frameTime
        y += vy * speed * gctx.frameTime

        if (x < -100f || x > gctx.metrics.width + 100f || y < -100f || y > gctx.metrics.height + 100f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.BULLET)
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawCircle(x, y, radius, paint)
    }

    private fun setDirection(dirX: Float, dirY: Float) {
        val length = sqrt(dirX * dirX + dirY * dirY)

        if (length <= 0.0001f) {
            vx = 1f
            vy = 0f
            return
        }

        vx = dirX / length
        vy = dirY / length
    }
}