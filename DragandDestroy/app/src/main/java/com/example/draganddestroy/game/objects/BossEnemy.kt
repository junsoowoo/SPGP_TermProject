package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class BossEnemy(
    gctx: GameContext,
    startX: Float,
    startY: Float,
    hp: Int,
    rewardGold: Int,
    radius: Float,
    speed: Float,
    attackDamage: Int,
    fireInterval: Float,
) : Enemy(gctx, startX, startY, hp, hp, rewardGold, radius, speed, true, attackDamage, fireInterval) {

    private var targetX = startX
    private var patternTimer = 0f
    private var aimedTimer = 0f
    private var spreadTimer = 0f
    private var circleTimer = 0f
    private var rushTimer = 0f

    private var rushMode = false
    private var rushDirY = 1f

    private val bossSprite = Sprite(gctx, R.drawable.boss_enemy)

    private val bossTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val phaseTextPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 26f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val hpBgPaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 18f
        strokeCap = Paint.Cap.ROUND
    }

    private val hpPaint = Paint().apply {
        color = Color.rgb(255, 80, 180)
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
    }

    private val hpRatio: Float
        get() = hp.toFloat() / maxHp.toFloat()

    private val phase: Int
        get() = when {
            hpRatio <= 0.33f -> 3
            hpRatio <= 0.66f -> 2
            else -> 1
        }

    init {
        bossSprite.setSize(radius * 3.0f, radius * 3.0f)
        bossSprite.setCenter(x, y)
    }

    override val collisionDamage: Int
        get() = attackDamage * 3

    override fun update(gctx: GameContext) {
        moveTime += gctx.frameTime
        patternTimer += gctx.frameTime
        aimedTimer += gctx.frameTime
        spreadTimer += gctx.frameTime
        circleTimer += gctx.frameTime
        rushTimer += gctx.frameTime
        fireTimer += gctx.frameTime

        targetX = gctx.metrics.width - 260f

        if (x > targetX) {
            x -= speed * gctx.frameTime
        } else {
            x = targetX
        }

        updateBossMove(gctx)
    }

    private fun updateBossMove(gctx: GameContext) {
        val phaseMovePower = when (phase) {
            1 -> 150f
            2 -> 220f
            else -> 280f
        }

        val phaseMoveSpeed = when (phase) {
            1 -> 1.8f
            2 -> 2.5f
            else -> 3.4f
        }

        if (phase >= 3 && rushTimer >= 6.0f) {
            rushMode = true
            rushTimer = 0f
            rushDirY *= -1f
        }

        if (rushMode) {
            y += rushDirY * 650f * gctx.frameTime

            if (y < radius + 30f) {
                y = radius + 30f
                rushMode = false
            }

            if (y > gctx.metrics.height - radius - 30f) {
                y = gctx.metrics.height - radius - 30f
                rushMode = false
            }
        } else {
            y = gctx.metrics.height * 0.5f + sin((moveTime * phaseMoveSpeed).toDouble()).toFloat() * phaseMovePower
        }

        if (y < radius) y = radius
        if (y > gctx.metrics.height - radius) y = gctx.metrics.height - radius
    }

    override fun draw(canvas: Canvas) {
        val hpBarLeft = 420f
        val hpBarRight = 1500f
        val hpBarY = 55f

        canvas.drawLine(hpBarLeft, hpBarY, hpBarRight, hpBarY, hpBgPaint)
        canvas.drawLine(hpBarLeft, hpBarY, hpBarLeft + (hpBarRight - hpBarLeft) * hpRatio.coerceIn(0f, 1f), hpBarY, hpPaint)

        bossSprite.setCenter(x, y)
        bossSprite.draw(canvas)

        canvas.drawText("BOSS", x, y + radius + 18f, bossTextPaint)
        canvas.drawText("PHASE $phase", x, y + radius + 52f, phaseTextPaint)
    }

    override fun tryFireAt(targetX: Float, targetY: Float): EnemyBullet? {
        return null
    }

    fun firePatterns(targetX: Float, targetY: Float): List<EnemyBullet> {
        if (isDead) return emptyList()

        val bullets = ArrayList<EnemyBullet>()

        val aimedInterval = when (phase) {
            1 -> 0.85f
            2 -> 0.65f
            else -> 0.48f
        }

        val spreadInterval = when (phase) {
            1 -> 2.4f
            2 -> 1.8f
            else -> 1.25f
        }

        val circleInterval = when (phase) {
            1 -> 5.2f
            2 -> 4.0f
            else -> 3.0f
        }

        if (aimedTimer >= aimedInterval) {
            aimedTimer = 0f
            addAimedShot(bullets, targetX, targetY)
        }

        if (spreadTimer >= spreadInterval) {
            spreadTimer = 0f
            addSpreadShot(bullets, targetX, targetY)
        }

        if (circleTimer >= circleInterval) {
            circleTimer = 0f
            addCircleShot(bullets)
        }

        return bullets
    }

    private fun addAimedShot(bullets: ArrayList<EnemyBullet>, targetX: Float, targetY: Float) {
        val count = if (phase >= 2) 2 else 1

        for (i in 0 until count) {
            val offsetY = (i - (count - 1) * 0.5f) * 35f
            bullets.add(EnemyBullet(startX = x - radius, startY = y + offsetY, dirX = targetX - x, dirY = targetY - y + offsetY, damage = attackDamage, speed = 620f + phase * 60f, radius = 13f, color = Color.rgb(255, 70, 70)))
        }
    }

    private fun addSpreadShot(bullets: ArrayList<EnemyBullet>, targetX: Float, targetY: Float) {
        val baseDirX = targetX - x
        val baseDirY = targetY - y

        val spreadCount = when (phase) {
            1 -> 3
            2 -> 5
            else -> 7
        }

        val angleStep = 12.0 * PI / 180.0
        val startIndex = -(spreadCount / 2)

        for (i in 0 until spreadCount) {
            val angle = (startIndex + i) * angleStep
            val cosA = cos(angle).toFloat()
            val sinA = sin(angle).toFloat()

            val dirX = baseDirX * cosA - baseDirY * sinA
            val dirY = baseDirX * sinA + baseDirY * cosA

            bullets.add(EnemyBullet(startX = x - radius, startY = y, dirX = dirX, dirY = dirY, damage = attackDamage + phase * 2, speed = 520f + phase * 45f, radius = 12f, color = Color.rgb(255, 140, 40)))
        }
    }

    private fun addCircleShot(bullets: ArrayList<EnemyBullet>) {
        val count = when (phase) {
            1 -> 10
            2 -> 16
            else -> 24
        }

        for (i in 0 until count) {
            val angle = (2.0 * PI * i / count).toFloat()
            val dirX = cos(angle)
            val dirY = sin(angle)

            bullets.add(EnemyBullet(startX = x, startY = y, dirX = dirX, dirY = dirY, damage = attackDamage + phase * 3, speed = 380f + phase * 40f, radius = 10f, color = Color.rgb(255, 40, 180)))
        }
    }
}