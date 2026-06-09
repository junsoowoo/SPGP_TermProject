package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.data.EnemyType
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sin

open class Enemy(
    startX: Float,
    startY: Float,
    protected var hp: Int,
    protected val maxHp: Int,
    val rewardGold: Int,
    open val radius: Float,
    protected val speed: Float,
    protected val isBoss: Boolean,
    protected val attackDamage: Int,
    protected val fireInterval: Float,
    val enemyType: EnemyType = EnemyType.NORMAL,
) : IGameObject {

    open var x = startX
        protected set

    open var y = startY
        protected set

    protected val baseY = startY

    open var isDead = false
        protected set

    protected var moveTime = 0f
    protected var fireTimer = fireInterval * 0.5f

    open val collisionDamage: Int
        get() = if (isBoss) attackDamage * 2 else attackDamage

    protected val bodyPaint = Paint().apply {
        color = when (enemyType) {
            EnemyType.NORMAL -> Color.RED
            EnemyType.FAST -> Color.rgb(255, 140, 40)
            EnemyType.TANK -> Color.rgb(120, 80, 255)
        }
        isAntiAlias = true
    }

    protected val eyePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    protected val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 22f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        moveTime += gctx.frameTime
        fireTimer += gctx.frameTime

        x -= speed * gctx.frameTime

        val wavePower = when (enemyType) {
            EnemyType.NORMAL -> 35f
            EnemyType.FAST -> 55f
            EnemyType.TANK -> 20f
        }

        val waveSpeed = when (enemyType) {
            EnemyType.NORMAL -> 4.0f
            EnemyType.FAST -> 6.0f
            EnemyType.TANK -> 2.5f
        }

        y = baseY + sin(moveTime * waveSpeed) * wavePower

        if (y < radius) y = radius
        if (y > gctx.metrics.height - radius) y = gctx.metrics.height - radius

        if (x < -radius - 50f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.ENEMY)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, bodyPaint)

        val label = when (enemyType) {
            EnemyType.NORMAL -> "N"
            EnemyType.FAST -> "F"
            EnemyType.TANK -> "T"
        }

        canvas.drawText(label, x, y + 8f, textPaint)
        canvas.drawCircle(x - radius * 0.28f, y - radius * 0.25f, 5f, eyePaint)
        canvas.drawCircle(x + radius * 0.28f, y - radius * 0.25f, 5f, eyePaint)
    }

    open fun takeDamage(damage: Int) {
        hp -= damage

        if (hp <= 0) {
            hp = 0
            isDead = true
        }
    }

    open fun tryFireAt(targetX: Float, targetY: Float): EnemyBullet? {
        if (isDead) return null
        if (x <= MainScene.PLAYER_CONTROL_WIDTH + radius) return null
        if (fireTimer < fireInterval) return null

        fireTimer = 0f
        return EnemyBullet(startX = x - radius, startY = y, dirX = targetX - x, dirY = targetY - y, damage = attackDamage)
    }
}