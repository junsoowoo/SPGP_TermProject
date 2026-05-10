package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Bullet(
    startX: Float,
    startY: Float,
    val damage: Int,
    private val color: Int = Color.YELLOW,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 12f

    var isDead = false

    private val speed = 1300f

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        x += speed * gctx.frameTime

        if (x > gctx.metrics.width + 100f) {
            isDead = true

            val scene = gctx.scene as? com.example.draganddestroy.game.scene.MainScene
            scene?.world?.remove(this, com.example.draganddestroy.game.scene.MainScene.Layer.BULLET)
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawCircle(x, y, radius, paint)
    }
}