package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class PauseScene(gctx: GameContext) : Scene(gctx) {

    override val isTransparent = true

    private val resumeButton = RectF(500f, 260f, 1100f, 360f)
    private val selectButton = RectF(500f, 410f, 1100f, 510f)
    private val exitButton = RectF(500f, 560f, 1100f, 660f)

    private val overlayPaint = Paint().apply {
        color = Color.argb(165, 0, 0, 0)
    }

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 64f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val buttonPaint = Paint().apply {
        color = Color.rgb(60, 70, 120)
        isAntiAlias = true
    }

    private val exitPaint = Paint().apply {
        color = Color.rgb(140, 50, 50)
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, overlayPaint)

        canvas.drawText("PAUSED", gctx.metrics.width / 2f, 170f, titlePaint)

        drawButton(canvas, resumeButton, "Return to Game", buttonPaint)
        drawButton(canvas, selectButton, "Go to Stage Select", buttonPaint)
        drawButton(canvas, exitButton, "Exit Game", exitPaint)
    }

    private fun drawButton(canvas: Canvas, rect: RectF, text: String, paint: Paint) {
        canvas.drawRoundRect(rect, 24f, 24f, paint)
        canvas.drawText(text, rect.centerX(), rect.centerY() + 13f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN) return true

        val point = gctx.metrics.fromScreen(event.x, event.y)
        val x = point.x
        val y = point.y

        when {
            resumeButton.contains(x, y) -> {
                gctx.sceneStack.pop()
            }

            selectButton.contains(x, y) -> {
                gctx.sceneStack.change(StageSelectScene(gctx))
            }

            exitButton.contains(x, y) -> {
                gctx.sceneStack.popAll()
            }
        }

        return true
    }

    override fun onBackPressed(): Boolean {
        gctx.sceneStack.pop()
        return true
    }
}