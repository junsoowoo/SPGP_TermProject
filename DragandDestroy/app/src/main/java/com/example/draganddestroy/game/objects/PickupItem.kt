package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.data.PickupType
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class PickupItem(
    startX: Float,
    startY: Float,
    val type: PickupType,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 24f
    var isDead = false

    private val speed = 230f
    private var floatTime = 0f

    private val bodyPaint = Paint().apply {
        color = when (type) {
            PickupType.MAGNET -> Color.rgb(70, 160, 255)
            PickupType.HEAL -> Color.rgb(80, 255, 120)
        }
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 22f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        floatTime += gctx.frameTime

        x -= speed * gctx.frameTime
        y += kotlin.math.sin(floatTime * 5f) * 25f * gctx.frameTime

        if (x < -60f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.PICKUP)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, bodyPaint)
        canvas.drawText(if (type == PickupType.MAGNET) "M" else "+", x, y + 8f, textPaint)
    }
}