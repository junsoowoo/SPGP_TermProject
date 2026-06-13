package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import com.example.draganddestroy.R
import com.example.draganddestroy.game.util.GameSound
import com.example.draganddestroy.game.data.GameStats
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
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
    private var hitFlashTime = 0f

    private val sprite = Sprite(gctx, R.drawable.player_ship)

    init {
        sprite.setSize(120f, 88f)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        if (hitFlashTime > 0f) {
            hitFlashTime -= gctx.frameTime
            if (hitFlashTime < 0f) hitFlashTime = 0f
        }

        if (isDead) return

        fireTimer += gctx.frameTime

        x += moveDirX * GameStats.playerMoveSpeed * movePower * gctx.frameTime
        y += moveDirY * GameStats.playerMoveSpeed * movePower * gctx.frameTime

        x = x.coerceIn(radius, gctx.metrics.width - radius)
        y = y.coerceIn(radius, gctx.metrics.height - radius)

        sprite.setCenter(x, y)
    }

    override fun draw(canvas: Canvas) {
        if (hitFlashTime > 0f && ((hitFlashTime * 24f).toInt() % 2 == 0)) return

        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }

    fun setMoveInput(dirX: Float, dirY: Float, power: Float) {
        val length = sqrt(dirX * dirX + dyFix(dirY))

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
        GameSound.playPlayerShot(gctx)

        return Bullet(gctx = gctx, startX = x + 80f, startY = y, damage = GameStats.playerDamage, color = Color.YELLOW, dirX = 1f, dirY = 0f, speed = 1500f)
    }

    fun takeDamage(damage: Int) {
        if (isDead) return

        hp -= damage
        hitFlashTime = HIT_FLASH_DURATION

        if (hp <= 0) {
            hp = 0
            isDead = true
        }
    }

    fun heal(amount: Int) {
        if (isDead) return

        hp = (hp + amount).coerceAtMost(maxHp)
    }

    private fun dyFix(dirY: Float): Float {
        return dirY * dirY
    }

    companion object {
        private const val HIT_FLASH_DURATION = 0.35f
    }
}