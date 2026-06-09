package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.game.data.DebugCommand
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.StageManager
import com.example.draganddestroy.game.data.TurretType
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class StageSelectScene(gctx: GameContext) : Scene(gctx) {

    private val basicTurretButton = RectF(300f, 150f, 760f, 240f)
    private val rapidTurretButton = RectF(840f, 150f, 1300f, 240f)

    private val stage1Button = RectF(420f, 300f, 1180f, 370f)
    private val stage2Button = RectF(420f, 390f, 1180f, 460f)
    private val stage3Button = RectF(420f, 480f, 1180f, 550f)
    private val stage4Button = RectF(420f, 570f, 1180f, 640f)
    private val bossButton = RectF(420f, 660f, 1180f, 730f)
    private val storeButton = RectF(420f, 760f, 1180f, 830f)

    private val bgPaint = Paint().apply {
        color = Color.rgb(10, 10, 30)
    }

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 54f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 30f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val smallTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 24f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val buttonPaint = Paint().apply {
        color = Color.rgb(50, 70, 120)
        isAntiAlias = true
    }

    private val selectedPaint = Paint().apply {
        color = Color.rgb(70, 150, 100)
        isAntiAlias = true
    }

    private val bossPaint = Paint().apply {
        color = Color.rgb(120, 50, 100)
        isAntiAlias = true
    }

    private val storePaint = Paint().apply {
        color = Color.rgb(50, 120, 70)
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        DebugCommand.consumeStageRequest()?.let {
            startStage(it)
            return
        }

        if (DebugCommand.consumeStoreRequest()) {
            gctx.sceneStack.change(StoreScene(gctx))
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, bgPaint)

        canvas.drawText("Drag and Destroy", gctx.metrics.width / 2f, 75f, titlePaint)
        canvas.drawText("Select Turret Type", gctx.metrics.width / 2f, 120f, textPaint)

        drawTurretButton(canvas, basicTurretButton, "Basic Turret", "Slow / High Damage", TurretType.BASIC)
        drawTurretButton(canvas, rapidTurretButton, "Rapid Turret", "Fast / Low Damage", TurretType.RAPID)

        drawButton(canvas, stage1Button, "1. Stage 1", buttonPaint)
        drawButton(canvas, stage2Button, "2. Stage 2", buttonPaint)
        drawButton(canvas, stage3Button, "3. Stage 3", buttonPaint)
        drawButton(canvas, stage4Button, "4. Stage 4", buttonPaint)
        drawButton(canvas, bossButton, "5. Boss Stage", bossPaint)
        drawButton(canvas, storeButton, "6. Store", storePaint)

        canvas.drawText("Selected: ${if (GameStats.selectedTurretType == TurretType.BASIC) "Basic Turret" else "Rapid Turret"}", gctx.metrics.width / 2f, 875f, smallTextPaint)
    }

    private fun drawTurretButton(canvas: Canvas, rect: RectF, title: String, desc: String, type: TurretType) {
        val paint = if (GameStats.selectedTurretType == type) selectedPaint else buttonPaint

        canvas.drawRoundRect(rect, 20f, 20f, paint)
        canvas.drawText(title, rect.centerX(), rect.centerY() - 8f, textPaint)
        canvas.drawText(desc, rect.centerX(), rect.centerY() + 24f, smallTextPaint)
    }

    private fun drawButton(canvas: Canvas, rect: RectF, text: String, paint: Paint) {
        canvas.drawRoundRect(rect, 20f, 20f, paint)
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

            stage1Button.contains(x, y) -> startStage(1)
            stage2Button.contains(x, y) -> startStage(2)
            stage3Button.contains(x, y) -> startStage(3)
            stage4Button.contains(x, y) -> startStage(4)
            bossButton.contains(x, y) -> startStage(5)
            storeButton.contains(x, y) -> gctx.sceneStack.change(StoreScene(gctx))
        }

        return true
    }

    private fun startStage(stageNumber: Int) {
        StageManager.setStageNumber(stageNumber)
        gctx.sceneStack.change(MainScene(gctx))
    }
}