package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.DebugCommand
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import com.example.draganddestroy.game.data.TurretType
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.draganddestroy.game.util.GameSound

class StageSelectScene(gctx: GameContext) : Scene(gctx) {

    private val farBg = Sprite(gctx, R.drawable.bg_space_far)
    private val nearBg = Sprite(gctx, R.drawable.bg_space_near)
    private val logoSprite = Sprite(gctx, R.drawable.title_logo)
    private val nextSprite = Sprite(gctx, R.drawable.btn_next_stage)

    private val rootPanel = RectF(250f, 140f, 1350f, 805f)
    private val turretPanel = RectF(285f, 185f, 560f, 665f)
    private val stagePanel = RectF(595f, 185f, 1315f, 665f)

    private val basicTurretButton = RectF(315f, 260f, 530f, 350f)
    private val rapidTurretButton = RectF(315f, 385f, 530f, 475f)

    private val stage1Button = RectF(630f, 260f, 925f, 355f)
    private val stage2Button = RectF(965f, 260f, 1260f, 355f)
    private val stage3Button = RectF(630f, 390f, 925f, 485f)
    private val stage4Button = RectF(965f, 390f, 1260f, 485f)
    private val bossButton = RectF(630f, 525f, 1260f, 595f)

    private val storeButton = RectF(650f, 690f, 930f, 760f)
    private val nextButton = RectF(1010f, 675f, 1260f, 775f)

    private var selectedStageNumber = 1

    private val dimPaint = Paint().apply {
        color = Color.argb(95, 0, 0, 0)
    }

    private val rootPanelPaint = Paint().apply {
        color = Color.argb(118, 10, 18, 42)
        isAntiAlias = true
    }

    private val sectionPanelPaint = Paint().apply {
        color = Color.argb(138, 18, 28, 58)
        isAntiAlias = true
    }

    private val sectionStrokePaint = Paint().apply {
        color = Color.argb(210, 132, 190, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val cardPaint = Paint().apply {
        color = Color.argb(180, 52, 74, 128)
        isAntiAlias = true
    }

    private val selectedPaint = Paint().apply {
        color = Color.argb(205, 58, 148, 118)
        isAntiAlias = true
    }

    private val bossPaint = Paint().apply {
        color = Color.argb(205, 120, 50, 120)
        isAntiAlias = true
    }

    private val storePaint = Paint().apply {
        color = Color.argb(205, 44, 126, 86)
        isAntiAlias = true
    }

    private val cardStrokePaint = Paint().apply {
        color = Color.argb(210, 170, 215, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val selectedStrokePaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val sectionTitlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 26f
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 27f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val subTextPaint = Paint().apply {
        color = Color.argb(230, 220, 235, 255)
        textSize = 20f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val summaryTextPaint = Paint().apply {
        color = Color.argb(235, 230, 240, 255)
        textSize = 23f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        logoSprite.setCenterProportionalWidth(gctx.metrics.width / 2f, 70f, 330f)
        nextSprite.setCenterProportionalWidth(nextButton.centerX(), nextButton.centerY(), 185f)
    }

    override fun onEnter() {
        GameSound.startBgm(gctx)
    }

    override fun update(gctx: GameContext) {
        DebugCommand.consumeStageRequest()?.let {
            selectedStageNumber = it
            startSelectedStage()
            return
        }

        if (DebugCommand.consumeStoreRequest()) {
            gctx.sceneStack.change(StoreScene(gctx))
        }
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas)

        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, dimPaint)
        canvas.drawRoundRect(rootPanel, 28f, 28f, rootPanelPaint)

        logoSprite.draw(canvas)

        canvas.drawRoundRect(turretPanel, 24f, 24f, sectionPanelPaint)
        canvas.drawRoundRect(turretPanel, 24f, 24f, sectionStrokePaint)

        canvas.drawRoundRect(stagePanel, 24f, 24f, sectionPanelPaint)
        canvas.drawRoundRect(stagePanel, 24f, 24f, sectionStrokePaint)

        canvas.drawText("LOADOUT", turretPanel.left + 22f, turretPanel.top + 34f, sectionTitlePaint)
        canvas.drawText("SELECT STAGE", stagePanel.left + 22f, stagePanel.top + 34f, sectionTitlePaint)

        drawTurretCard(canvas, basicTurretButton, "Basic", "High Damage", TurretType.BASIC)
        drawTurretCard(canvas, rapidTurretButton, "Rapid", "Fast Attack", TurretType.RAPID)

        drawStageCard(canvas, stage1Button, 1, "Stage 1", "Warm-up", false)
        drawStageCard(canvas, stage2Button, 2, "Stage 2", "Fast Enemy", false)
        drawStageCard(canvas, stage3Button, 3, "Stage 3", "Dense Wave", false)
        drawStageCard(canvas, stage4Button, 4, "Stage 4", "Heavy Wave", false)
        drawStageCard(canvas, bossButton, 5, "Boss Stage", "Final Battle", true)

        drawActionCard(canvas, storeButton, "STORE", storePaint)
        nextSprite.draw(canvas)

        canvas.drawText("Selected Turret : ${if (GameStats.selectedTurretType == TurretType.BASIC) "Basic" else "Rapid"}", 425f, 610f, summaryTextPaint)
        canvas.drawText("Selected Stage : ${getStageTitle(selectedStageNumber)}", 955f, 635f, summaryTextPaint)
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

    private fun drawTurretCard(canvas: Canvas, rect: RectF, title: String, desc: String, type: TurretType) {
        val selected = GameStats.selectedTurretType == type
        canvas.drawRoundRect(rect, 20f, 20f, if (selected) selectedPaint else cardPaint)
        canvas.drawRoundRect(rect, 20f, 20f, if (selected) selectedStrokePaint else cardStrokePaint)
        canvas.drawText(title, rect.centerX(), rect.centerY() - 8f, textPaint)
        canvas.drawText(desc, rect.centerX(), rect.centerY() + 25f, subTextPaint)
    }

    private fun drawStageCard(canvas: Canvas, rect: RectF, stageNumber: Int, title: String, desc: String, isBoss: Boolean) {
        val selected = selectedStageNumber == stageNumber
        val fill = when {
            selected -> selectedPaint
            isBoss -> bossPaint
            else -> cardPaint
        }

        canvas.drawRoundRect(rect, 22f, 22f, fill)
        canvas.drawRoundRect(rect, 22f, 22f, if (selected) selectedStrokePaint else cardStrokePaint)
        canvas.drawText(title, rect.centerX(), rect.centerY() - 8f, textPaint)
        canvas.drawText(desc, rect.centerX(), rect.centerY() + 24f, subTextPaint)
    }

    private fun drawActionCard(canvas: Canvas, rect: RectF, text: String, paint: Paint) {
        canvas.drawRoundRect(rect, 22f, 22f, paint)
        canvas.drawRoundRect(rect, 22f, 22f, cardStrokePaint)
        canvas.drawText(text, rect.centerX(), rect.centerY() + 10f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN) return true

        val point = gctx.metrics.fromScreen(event.x, event.y)
        val x = point.x
        val y = point.y

        when {
            basicTurretButton.contains(x, y) -> GameStats.selectedTurretType = TurretType.BASIC
            rapidTurretButton.contains(x, y) -> GameStats.selectedTurretType = TurretType.RAPID

            stage1Button.contains(x, y) -> selectedStageNumber = 1
            stage2Button.contains(x, y) -> selectedStageNumber = 2
            stage3Button.contains(x, y) -> selectedStageNumber = 3
            stage4Button.contains(x, y) -> selectedStageNumber = 4
            bossButton.contains(x, y) -> selectedStageNumber = 5

            storeButton.contains(x, y) -> gctx.sceneStack.change(StoreScene(gctx))
            nextButton.contains(x, y) -> startSelectedStage()
        }

        return true
    }

    private fun startSelectedStage() {
        StageManager.setStageNumber(selectedStageNumber)
        gctx.sceneStack.change(MainScene(gctx))
    }

    private fun getStageTitle(stageNumber: Int): String {
        return when (stageNumber) {
            1 -> "Stage 1"
            2 -> "Stage 2"
            3 -> "Stage 3"
            4 -> "Stage 4"
            5 -> "Boss Stage"
            else -> "Stage 1"
        }
    }
}