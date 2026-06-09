package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class DragPathEffect(
    startX: Float,
    startY: Float,
) : IGameObject {

    private val path = Path()

    private val paint = Paint().apply {
        color = Color.argb(180, 0, 255, 255)
        strokeWidth = 12f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    init {
        path.moveTo(startX, startY)
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    fun addPoint(x: Float, y: Float) {
        path.lineTo(x, y)
    }
}