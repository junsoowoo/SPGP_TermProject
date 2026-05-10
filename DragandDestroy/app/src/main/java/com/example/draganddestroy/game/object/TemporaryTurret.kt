package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class TemporaryTurret(
    private val gctx: GameContext,
    private val world: World<MainScene.Layer>,
    private val startX: Float,
    private val startY: Float,
    private val pathEffect: DragPathEffect,
) : IGameObject {

    private var x = startX
    private var y = startY

    private var lifeTime = 4.0f
    private var fireTimer = 0f

    private val fireInterval = 0.18f

    var isDead = false
        private set

    private val bodyPaint = Paint().apply {
        color = Color.MAGENTA
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 26f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        lifeTime -= gctx.frameTime
        fireTimer += gctx.frameTime

        if (fireTimer >= fireInterval) {
            fireTimer = 0f

            world.add(
                Bullet(
                    startX = x + 45f,
                    startY = y,
                    damage = 20,
                    color = Color.MAGENTA
                ),
                MainScene.Layer.BULLET
            )
        }

        if (lifeTime <= 0f) {
            isDead = true
            world.remove(this, MainScene.Layer.TURRET)
        }
    }

    override fun draw(canvas: Canvas) {
        pathEffect.draw(canvas)

        canvas.drawCircle(x, y, 35f, bodyPaint)
        canvas.drawText(String.format("%.1f", lifeTime), x, y - 50f, textPaint)
    }
}