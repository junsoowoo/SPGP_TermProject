package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class ResultScene(
    gctx: GameContext,
    private val success: Boolean,
) : Scene(gctx) {

    private val farBg = Sprite(gctx, R.drawable.bg_space_far)
    private val nearBg = Sprite(gctx, R.drawable.bg_space_near)

    private val panel = RectF(430f, 115f, 1170f, 820f)
    private val retryButton = RectF(650f, 685f, 950f, 775f)

    private val resultSprite = Sprite(gctx, if (success) R.drawable.result_clear else R.drawable.result_gameover)
    private val restartSprite = Sprite(gctx, R.drawable.btn_restart)

    private val dimPaint = Paint().apply {
        color = Color.argb(130, 0, 0, 0)
    }

    private val panelPaint = Paint().apply {
        color = Color.argb(140, 12, 20, 45)
        isAntiAlias = true
    }

    private val panelStrokePaint = Paint().apply {
        color = Color.argb(220, 150, 210, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 32f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val smallTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 26f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        farBg.setSize(gctx.metrics.width, gctx.metrics.height)
        farBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)

        nearBg.setSize(gctx.metrics.width, gctx.metrics.height)
        nearBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)

        resultSprite.setCenterProportionalWidth(gctx.metrics.width / 2f, 170f, 420f)
        restartSprite.setCenterProportionalWidth(retryButton.centerX(), retryButton.centerY(), 210f)
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas)

        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, dimPaint)
        canvas.drawRoundRect(panel, 28f, 28f, panelPaint)
        canvas.drawRoundRect(panel, 28f, 28f, panelStrokePaint)

        resultSprite.draw(canvas)

        canvas.drawText("Final Coin : ${GameStats.gold}", panel.centerX(), 300f, textPaint)
        canvas.drawText("Selected Turret : ${GameStats.selectedTurretType}", panel.centerX(), 345f, smallTextPaint)

        canvas.drawText("Player Damage Lv.${GameStats.playerDamageLevel}", panel.centerX(), 410f, smallTextPaint)
        canvas.drawText("Player Max HP Lv.${GameStats.playerMaxHpLevel}", panel.centerX(), 450f, smallTextPaint)
        canvas.drawText("Player Coin Gain Lv.${GameStats.playerCoinGainLevel}", panel.centerX(), 490f, smallTextPaint)
        canvas.drawText("Player Move Speed Lv.${GameStats.playerMoveSpeedLevel}", panel.centerX(), 530f, smallTextPaint)
        canvas.drawText("Player Fire Rate Lv.${GameStats.playerFireRateLevel}", panel.centerX(), 570f, smallTextPaint)

        canvas.drawText("Turret Range Lv.${GameStats.turretRangeLevel}", panel.centerX(), 620f, smallTextPaint)
        canvas.drawText("Turret Cost Down Lv.${GameStats.turretCostLevel}", panel.centerX(), 660f, smallTextPaint)

        restartSprite.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN) return true

        val point = gctx.metrics.fromScreen(event.x, event.y)

        if (retryButton.contains(point.x, point.y)) {
            GameStats.resetAll()
            StageManager.reset()
            gctx.sceneStack.change(StageSelectScene(gctx))
        }

        return true
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.rgb(6, 8, 18))

        farBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)
        farBg.draw(canvas)

        nearBg.setCenter(gctx.metrics.width / 2f, gctx.metrics.height / 2f)
        nearBg.draw(canvas)
    }
}