package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.draganddestroy.game.util.GameSound

class PauseScene(gctx: GameContext) : Scene(gctx) {

    override val isTransparent = true

    private val panel = RectF(500f, 150f, 1100f, 760f)
    private val resumeButton = RectF(650f, 300f, 950f, 385f)
    private val selectButton = RectF(650f, 445f, 950f, 530f)
    private val exitButton = RectF(650f, 590f, 950f, 675f)

    private val resumeSprite = Sprite(gctx, R.drawable.btn_resume)
    private val selectSprite = Sprite(gctx, R.drawable.btn_stage_select)
    private val exitSprite = Sprite(gctx, R.drawable.btn_exit)

    private val overlayPaint = Paint().apply {
        color = Color.argb(165, 0, 0, 0)
    }

    private val panelPaint = Paint().apply {
        color = Color.argb(145, 12, 20, 45)
        isAntiAlias = true
    }

    private val panelStrokePaint = Paint().apply {
        color = Color.argb(220, 150, 210, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 58f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        resumeSprite.setCenterProportionalWidth(resumeButton.centerX(), resumeButton.centerY(), 210f)
        selectSprite.setCenterProportionalWidth(selectButton.centerX(), selectButton.centerY(), 210f)
        exitSprite.setCenterProportionalWidth(exitButton.centerX(), exitButton.centerY(), 210f)
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, overlayPaint)

        canvas.drawRoundRect(panel, 28f, 28f, panelPaint)
        canvas.drawRoundRect(panel, 28f, 28f, panelStrokePaint)

        canvas.drawText("PAUSED", panel.centerX(), 245f, titlePaint)

        resumeSprite.draw(canvas)
        selectSprite.draw(canvas)
        exitSprite.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN) return true

        val point = gctx.metrics.fromScreen(event.x, event.y)
        val x = point.x
        val y = point.y

        when {
            resumeButton.contains(x, y) -> {
                GameSound.playButtonClick(gctx)
                gctx.sceneStack.pop()
            }

            selectButton.contains(x, y) -> {
                GameSound.playButtonClick(gctx)
                gctx.sceneStack.popAll(finishesActivity = false)
                gctx.sceneStack.push(StageSelectScene(gctx))
            }

            exitButton.contains(x, y) -> {
                GameSound.playButtonClick(gctx)
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