package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class ResultScene(
    gctx: GameContext,
    private val success: Boolean,
) : Scene(gctx) {

    private val retryButton = RectF(520f, 660f, 1080f, 760f)

    private val bgPaint = Paint().apply {
        color = Color.rgb(10, 10, 25)
    }

    private val titlePaint = Paint().apply {
        color = if (success) Color.WHITE else Color.RED
        textSize = 64f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 34f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val smallTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val buttonPaint = Paint().apply {
        color = Color.rgb(50, 120, 80)
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, bgPaint)

        canvas.drawText(if (success) "Mission Clear" else "Game Over", gctx.metrics.width / 2f, 130f, titlePaint)
        canvas.drawText("Final Coin: ${GameStats.gold}", gctx.metrics.width / 2f, 220f, textPaint)
        canvas.drawText("Selected Turret: ${GameStats.selectedTurretType}", gctx.metrics.width / 2f, 270f, smallTextPaint)

        canvas.drawText("Player Damage Lv.${GameStats.playerDamageLevel}", gctx.metrics.width / 2f, 340f, smallTextPaint)
        canvas.drawText("Player Max HP Lv.${GameStats.playerMaxHpLevel}", gctx.metrics.width / 2f, 380f, smallTextPaint)
        canvas.drawText("Player Coin Gain Lv.${GameStats.playerCoinGainLevel}", gctx.metrics.width / 2f, 420f, smallTextPaint)
        canvas.drawText("Player Move Speed Lv.${GameStats.playerMoveSpeedLevel}", gctx.metrics.width / 2f, 460f, smallTextPaint)
        canvas.drawText("Player Fire Rate Lv.${GameStats.playerFireRateLevel}", gctx.metrics.width / 2f, 500f, smallTextPaint)

        canvas.drawText("Turret Range Lv.${GameStats.turretRangeLevel}", gctx.metrics.width / 2f, 550f, smallTextPaint)
        canvas.drawText("Turret Cost Down Lv.${GameStats.turretCostLevel}", gctx.metrics.width / 2f, 590f, smallTextPaint)
        canvas.drawText("Turret HP Lv.${GameStats.turretHpLevel}", gctx.metrics.width / 2f, 630f, smallTextPaint)
        canvas.drawText("Turret Damage Lv.${GameStats.turretDamageLevel}", gctx.metrics.width / 2f, 670f, smallTextPaint)

        canvas.drawRoundRect(retryButton, 20f, 20f, buttonPaint)
        canvas.drawText("Restart", retryButton.centerX(), retryButton.centerY() + 12f, textPaint)
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
}