package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.EnemyType
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sin

open class Enemy(
    protected val gctx: GameContext,
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

    private val imageResId = when (enemyType) {
        EnemyType.NORMAL -> R.drawable.enemy_normal
        EnemyType.FAST -> R.drawable.enemy_fast
        EnemyType.TANK -> R.drawable.enemy_tank
    }

    protected val sprite = Sprite(gctx, imageResId)

    init {
        sprite.setSize(radius * 2.4f, radius * 2.4f)
        sprite.setCenter(x, y)
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

        y = baseY + sin((moveTime * waveSpeed).toDouble()).toFloat() * wavePower

        if (y < radius) y = radius
        if (y > gctx.metrics.height - radius) y = gctx.metrics.height - radius

        if (x < -radius - 50f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.ENEMY)
        }
    }

    override fun draw(canvas: Canvas) {
        sprite.setCenter(x, y)
        sprite.draw(canvas)
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
        return EnemyBullet(gctx = gctx, startX = x - radius, startY = y, dirX = targetX - x, dirY = targetY - y, damage = attackDamage)
    }
}