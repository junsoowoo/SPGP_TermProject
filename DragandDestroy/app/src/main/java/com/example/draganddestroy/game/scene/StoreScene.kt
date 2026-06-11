package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import com.example.draganddestroy.game.data.UpgradeType
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
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

    private val farBg = Sprite(gctx, R.drawable.bg_space_far)
    private val nearBg = Sprite(gctx, R.drawable.bg_space_near)
    private val bannerSprite = Sprite(gctx, R.drawable.store_banner)
    private val nextSprite = Sprite(gctx, R.drawable.btn_next_stage)
    private val stageSelectSprite = Sprite(gctx, R.drawable.btn_stage_select)

    private val rootPanel = RectF(190f, 120f, 1410f, 780f)
    private val playerPanel = RectF(230f, 200f, 760f, 575f)
    private val turretPanel = RectF(840f, 200f, 1370f, 515f)
    private val summaryPanel = RectF(495f, 610f, 1105f, 662f)

    private val buttons = listOf(
        UpgradeButton(RectF(255f, 245f, 735f, 295f), "Player Damage", UpgradeType.PLAYER_DAMAGE),
        UpgradeButton(RectF(255f, 305f, 735f, 355f), "Player Max HP", UpgradeType.PLAYER_MAX_HP),
        UpgradeButton(RectF(255f, 365f, 735f, 415f), "Coin Gain", UpgradeType.PLAYER_COIN_GAIN),
        UpgradeButton(RectF(255f, 425f, 735f, 475f), "Move Speed", UpgradeType.PLAYER_MOVE_SPEED),
        UpgradeButton(RectF(255f, 485f, 735f, 535f), "Fire Rate", UpgradeType.PLAYER_FIRE_RATE),

        UpgradeButton(RectF(865f, 245f, 1345f, 295f), "Turret Range", UpgradeType.TURRET_RANGE),
        UpgradeButton(RectF(865f, 305f, 1345f, 355f), "Turret Cost Down", UpgradeType.TURRET_COST),
        UpgradeButton(RectF(865f, 365f, 1345f, 415f), "Turret HP", UpgradeType.TURRET_HP),
        UpgradeButton(RectF(865f, 425f, 1345f, 475f), "Turret Damage", UpgradeType.TURRET_DAMAGE),
    )

    private val stageSelectButton = RectF(525f, 675f, 745f, 750f)
    private val nextButton = RectF(855f, 675f, 1075f, 750f)

    private var autoCloseTimer = 0f

    private val dimPaint = Paint().apply {
        color = Color.argb(115, 0, 0, 0)
    }

    private val rootPanelPaint = Paint().apply {
        color = Color.argb(118, 10, 18, 42)
        isAntiAlias = true
    }

    private val sectionPanelPaint = Paint().apply {
        color = Color.argb(138, 18, 28, 58)
        isAntiAlias = true
    }

    private val panelStrokePaint = Paint().apply {
        color = Color.argb(210, 132, 190, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val rowPaint = Paint().apply {
        color = Color.argb(182, 52, 74, 128)
        isAntiAlias = true
    }

    private val rowStrokePaint = Paint().apply {
        color = Color.argb(215, 175, 220, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val sectionTitlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 25f
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
    }

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 25f
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
    }

    private val valuePaint = Paint().apply {
        color = Color.WHITE
        textSize = 21f
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
    }

    private val centerTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 23f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        bannerSprite.setCenterProportionalWidth(gctx.metrics.width / 2f, 72f, 330f)
        nextSprite.setCenterProportionalWidth(nextButton.centerX(), nextButton.centerY(), 165f)
        stageSelectSprite.setCenterProportionalWidth(stageSelectButton.centerX(), stageSelectButton.centerY(), 165f)
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
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, dimPaint)
        } else {
            drawBackground(canvas)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, dimPaint)
        }

        canvas.drawRoundRect(rootPanel, 28f, 28f, rootPanelPaint)
        bannerSprite.draw(canvas)

        canvas.drawRoundRect(playerPanel, 24f, 24f, sectionPanelPaint)
        canvas.drawRoundRect(playerPanel, 24f, 24f, panelStrokePaint)

        canvas.drawRoundRect(turretPanel, 24f, 24f, sectionPanelPaint)
        canvas.drawRoundRect(turretPanel, 24f, 24f, panelStrokePaint)

        canvas.drawRoundRect(summaryPanel, 18f, 18f, sectionPanelPaint)
        canvas.drawRoundRect(summaryPanel, 18f, 18f, panelStrokePaint)

        canvas.drawText("PLAYER UPGRADES", playerPanel.left + 20f, playerPanel.top + 34f, sectionTitlePaint)
        canvas.drawText("TURRET UPGRADES", turretPanel.left + 20f, turretPanel.top + 34f, sectionTitlePaint)

        for (button in buttons) {
            drawUpgradeButton(canvas, button)
        }

        val summaryText = if (inStageShop) {
            "Coin ${GameStats.gold}    Stage Coin ${GameStats.stageGold}    Auto return ${((AUTO_CLOSE_TIME - autoCloseTimer).coerceAtLeast(0f)).toInt() + 1}"
        } else {
            "Coin ${GameStats.gold}    Stage Coin ${GameStats.stageGold}"
        }
        canvas.drawText(summaryText, summaryPanel.centerX(), summaryPanel.centerY() + 8f, centerTextPaint)

        stageSelectSprite.draw(canvas)
        nextSprite.draw(canvas)
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

    private fun drawUpgradeButton(canvas: Canvas, button: UpgradeButton) {
        val level = GameStats.getUpgradeLevel(button.type)
        val cost = GameStats.getUpgradeCost(button.type)

        canvas.drawRoundRect(button.rect, 16f, 16f, rowPaint)
        canvas.drawRoundRect(button.rect, 16f, 16f, rowStrokePaint)

        canvas.drawText(button.title, button.rect.left + 18f, button.rect.centerY() + 8f, titlePaint)
        canvas.drawText("Lv.$level", button.rect.right - 118f, button.rect.centerY() + 8f, valuePaint)
        canvas.drawText("Cost:$cost", button.rect.right - 16f, button.rect.centerY() + 8f, valuePaint)
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

        if (stageSelectButton.contains(x, y)) {
            goToStageSelect()
            return true
        }

        if (nextButton.contains(x, y)) {
            if (inStageShop) {
                gctx.sceneStack.pop()
            } else {
                StageManager.goNextStage()
                gctx.sceneStack.change(MainScene(gctx))
            }
            return true
        }

        return true
    }

    private fun goToStageSelect() {
        gctx.sceneStack.popAll(finishesActivity = false)
        gctx.sceneStack.push(StageSelectScene(gctx))
    }

    companion object {
        private const val AUTO_CLOSE_TIME = 3f
    }
}