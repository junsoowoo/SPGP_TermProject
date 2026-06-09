package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.game.data.GameStats
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt

class PlayerShip(
    private val gctx: GameContext,
) : IGameObject {

    var x = 140f
        private set

    var y = 450f
        private set

    var hp = GameStats.playerMaxHp
        private set

    val maxHp: Int
        get() = GameStats.playerMaxHp

    val radius = 48f

    var isDead = false
        private set

    private var moveDirX = 0f
    private var moveDirY = 0f
    private var movePower = 0f

    private var fireTimer = 0f

    private val bodyPaint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
    }

    private val wingPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private val gunPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        if (isDead) return

        fireTimer += gctx.frameTime

        x += moveDirX * GameStats.playerMoveSpeed * movePower * gctx.frameTime
        y += moveDirY * GameStats.playerMoveSpeed * movePower * gctx.frameTime

        x = x.coerceIn(radius, gctx.metrics.width - radius)
        y = y.coerceIn(radius, gctx.metrics.height - radius)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, 42f, bodyPaint)
        canvas.drawRect(x - 55f, y - 15f, x + 35f, y + 15f, wingPaint)
        canvas.drawRect(x + 30f, y - 8f, x + 75f, y + 8f, gunPaint)
    }

    fun setMoveInput(dirX: Float, dirY: Float, power: Float) {
        val length = sqrt(dirX * dirX + dirY * dirY)

        if (length <= 0.001f || power <= 0f) {
            moveDirX = 0f
            moveDirY = 0f
            movePower = 0f
            return
        }

        moveDirX = dirX / length
        moveDirY = dirY / length
        movePower = power.coerceIn(0f, 1f)
    }

    fun tryFire(): Bullet? {
        if (isDead) return null
        if (fireTimer < GameStats.playerFireInterval) return null

        fireTimer = 0f
        return Bullet(startX = x + 80f, startY = y, damage = GameStats.playerDamage, color = Color.YELLOW, dirX = 1f, dirY = 0f, speed = 1500f)
    }

    fun takeDamage(damage: Int) {
        if (isDead) return

        hp -= damage

        if (hp <= 0) {
            hp = 0
            isDead = true
        }
    }

    fun heal(amount: Int) {
        if (isDead) return

        hp = (hp + amount).coerceAtMost(maxHp)
    }
}