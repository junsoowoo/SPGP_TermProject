package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import com.example.draganddestroy.game.data.UpgradeType
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class StoreScene(
    gctx: GameContext,
    private val inStageShop: Boolean = false,
) : Scene(gctx) {

    override val isTransparent = inStageShop

    private data class UpgradeButton(
        val rect: RectF,
        val title: String,
        val type: UpgradeType,
    )

    private val buttons = listOf(
        UpgradeButton(RectF(120f, 160f, 760f, 230f), "Player Damage", UpgradeType.PLAYER_DAMAGE),
        UpgradeButton(RectF(120f, 250f, 760f, 320f), "Player Max HP", UpgradeType.PLAYER_MAX_HP),
        UpgradeButton(RectF(120f, 340f, 760f, 410f), "Coin Gain", UpgradeType.PLAYER_COIN_GAIN),
        UpgradeButton(RectF(120f, 430f, 760f, 500f), "Move Speed", UpgradeType.PLAYER_MOVE_SPEED),
        UpgradeButton(RectF(120f, 520f, 760f, 590f), "Fire Rate", UpgradeType.PLAYER_FIRE_RATE),

        UpgradeButton(RectF(840f, 160f, 1480f, 230f), "Turret Range", UpgradeType.TURRET_RANGE),
        UpgradeButton(RectF(840f, 250f, 1480f, 320f), "Turret Cost Down", UpgradeType.TURRET_COST),
        UpgradeButton(RectF(840f, 340f, 1480f, 410f), "Turret HP", UpgradeType.TURRET_HP),
        UpgradeButton(RectF(840f, 430f, 1480f, 500f), "Turret Damage", UpgradeType.TURRET_DAMAGE),
    )

    private val nextButton = RectF(520f, 720f, 1080f, 810f)

    private var autoCloseTimer = 0f

    private val bgPaint = Paint().apply {
        color = Color.rgb(20, 20, 35)
    }

    private val overlayPaint = Paint().apply {
        color = Color.argb(190, 0, 0, 0)
    }

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 52f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val buttonPaint = Paint().apply {
        color = Color.rgb(70, 70, 120)
        isAntiAlias = true
    }

    private val nextPaint = Paint().apply {
        color = Color.rgb(40, 120, 70)
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        if (!inStageShop) return

        autoCloseTimer += gctx.frameTime

        if (autoCloseTimer >= AUTO_CLOSE_TIME) {
            gctx.sceneStack.pop()
        }
    }

    override fun draw(canvas: Canvas) {
        if (inStageShop) {
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, overlayPaint)
        } else {
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, bgPaint)
        }

        canvas.drawText(if (inStageShop) "Supply Upgrade" else "Upgrade Store", gctx.metrics.width / 2f, 75f, titlePaint)
        canvas.drawText("Coin: ${GameStats.gold}   Stage Coin: ${GameStats.stageGold}", gctx.metrics.width / 2f, 120f, textPaint)

        if (inStageShop) {
            canvas.drawText("Auto return in ${(AUTO_CLOSE_TIME - autoCloseTimer).coerceAtLeast(0f).toInt() + 1}", gctx.metrics.width / 2f, 655f, textPaint)
        }

        for (button in buttons) {
            drawUpgradeButton(canvas, button)
        }

        canvas.drawRoundRect(nextButton, 20f, 20f, nextPaint)
        canvas.drawText(if (inStageShop) "Continue Game" else "Next Stage", nextButton.centerX(), nextButton.centerY() + 10f, textPaint)
    }

    private fun drawUpgradeButton(canvas: Canvas, button: UpgradeButton) {
        val level = GameStats.getUpgradeLevel(button.type)
        val cost = GameStats.getUpgradeCost(button.type)

        canvas.drawRoundRect(button.rect, 18f, 18f, buttonPaint)
        canvas.drawText("${button.title}  Lv.$level  Cost:$cost", button.rect.centerX(), button.rect.centerY() + 10f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN) return true

        val point = gctx.metrics.fromScreen(event.x, event.y)
        val x = point.x
        val y = point.y

        for (button in buttons) {
            if (button.rect.contains(x, y)) {
                GameStats.upgrade(button.type)

                if (inStageShop) {
                    gctx.sceneStack.pop()
                }

                return true
            }
        }

        if (nextButton.contains(x, y)) {
            if (inStageShop) {
                gctx.sceneStack.pop()
            } else {
                StageManager.goNextStage()
                gctx.sceneStack.change(MainScene(gctx))
            }
        }

        return true
    }

    companion object {
        private const val AUTO_CLOSE_TIME = 3f
    }
}