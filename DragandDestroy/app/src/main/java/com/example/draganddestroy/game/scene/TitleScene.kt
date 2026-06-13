package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sin
import com.example.draganddestroy.game.util.GameSound
class TitleScene(gctx: GameContext) : Scene(gctx) {

    private val farBg = Sprite(gctx, R.drawable.bg_space_far)
    private val nearBg = Sprite(gctx, R.drawable.bg_space_near)
    private val logoSprite = Sprite(gctx, R.drawable.title_logo)

    private var blinkTime = 0f

    private val touchTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 42f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val subTextPaint = Paint().apply {
        color = Color.argb(210, 220, 230, 255)
        textSize = 26f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        farBg.setSize(gctx.metrics.width, gctx.metrics.height)
        farBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)

        nearBg.setSize(gctx.metrics.width, gctx.metrics.height)
        nearBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)

        logoSprite.setCenterProportionalWidth(gctx.metrics.width / 2f, 275f, 520f)
    }

    override fun onEnter() {
        GameSound.startBgm(gctx)
    }

    override fun update(gctx: GameContext) {
        blinkTime += gctx.frameTime
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas)

        logoSprite.draw(canvas)

        val alpha = (150 + sin(blinkTime * 4.0f).toFloat() * 80f).toInt().coerceIn(70, 255)
        touchTextPaint.color = Color.argb(alpha, 255, 255, 255)

        canvas.drawText("터치해서 시작하세요", gctx.metrics.width / 2f, 600f, touchTextPaint)
        canvas.drawText("Drag to install turrets and destroy enemies", gctx.metrics.width / 2f, 655f, subTextPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            GameSound.playButtonClick(gctx)
            gctx.sceneStack.change(StageSelectScene(gctx))
        }

        return true
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.rgb(6, 8, 18))

        farBg.setSize(gctx.metrics.width, gctx.metrics.height)
        farBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)
        farBg.draw(canvas)

        nearBg.setSize(gctx.metrics.width, gctx.metrics.height)
        nearBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)
        nearBg.draw(canvas)
    }
}