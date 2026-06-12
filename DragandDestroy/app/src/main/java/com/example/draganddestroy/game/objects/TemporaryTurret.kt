package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.TurretType
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class TemporaryTurret(
    private val gctx: GameContext,
    private val world: World<MainScene.Layer>,
    startX: Float,
    startY: Float,
    private val type: TurretType,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = if (type == TurretType.BASIC) 18f else 15f
    val attackRange = if (type == TurretType.BASIC) GameStats.getBasicTurretRange() else GameStats.getRapidTurretRange()

    private var hp = if (type == TurretType.BASIC) GameStats.getBasicTurretHp() else GameStats.getRapidTurretHp()
    private var lifeTime = if (type == TurretType.BASIC) 4.7f else 3.9f
    private var fireTimer = 0f

    private val fireInterval = if (type == TurretType.BASIC) 1.05f else 0.22f
    private val bulletSpeed = if (type == TurretType.BASIC) 1000f else 1250f

    var isDead = false
        private set

    private var currentTargetX = x
    private var currentTargetY = y - 1f
    private var hasTarget = false

    private val sprite = Sprite(gctx, if (type == TurretType.BASIC) R.drawable.turret_basic else R.drawable.turret_rapid)

    private val rangePaint = Paint().apply {
        color = if (type == TurretType.BASIC) Color.argb(25, 255, 0, 255) else Color.argb(22, 60, 220, 255)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val rangeLinePaint = Paint().apply {
        color = if (type == TurretType.BASIC) Color.argb(85, 255, 0, 255) else Color.argb(85, 60, 220, 255)
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val aimPaint = Paint().apply {
        color = Color.argb(120, 255, 255, 255)
        strokeWidth = 3f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    init {
        val size = if (type == TurretType.BASIC) 90f else 84f
        sprite.setSize(size, size)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        lifeTime -= gctx.frameTime
        fireTimer += gctx.frameTime

        val target = findTargetInRange()
        hasTarget = target != null

        if (target != null) {
            currentTargetX = target.x
            currentTargetY = target.y

            if (fireTimer >= fireInterval) {
                fireTimer = 0f
                fireAt(target)
            }
        }

        if (lifeTime <= 0f || hp <= 0) {
            isDead = true
            world.remove(this, MainScene.Layer.TURRET)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, attackRange, rangePaint)
        canvas.drawCircle(x, y, attackRange, rangeLinePaint)

        if (hasTarget) {
            canvas.drawLine(x, y, currentTargetX, currentTargetY, aimPaint)
        }

        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }

    fun takeDamage(damage: Int) {
        hp -= damage

        if (hp <= 0) {
            hp = 0
            isDead = true
            world.remove(this, MainScene.Layer.TURRET)
        }
    }

    private fun findTargetInRange(): Enemy? {
        val enemies = world.objectsAt(MainScene.Layer.ENEMY)

        var bestEnemy: Enemy? = null
        var bestDistanceSq = attackRange * attackRange

        for (i in enemies.indices) {
            val enemy = enemies[i] as? Enemy ?: continue
            if (enemy.isDead) continue

            val dx = enemy.x - x
            val dy = enemy.y - y
            val distanceSq = dx * dx + dy * dy

            if (distanceSq <= bestDistanceSq) {
                bestDistanceSq = distanceSq
                bestEnemy = enemy
            }
        }

        return bestEnemy
    }

    private fun fireAt(enemy: Enemy) {
        val dirX = enemy.x - x
        val dirY = enemy.y - y
        val damage = if (type == TurretType.BASIC) GameStats.getBasicTurretDamage() else GameStats.getRapidTurretDamage()
        val color = if (type == TurretType.BASIC) Color.MAGENTA else Color.rgb(60, 220, 255)

        world.add(Bullet(gctx = gctx, startX = x, startY = y, damage = damage, color = color, dirX = dirX, dirY = dirY, speed = bulletSpeed), MainScene.Layer.BULLET)
    }
}