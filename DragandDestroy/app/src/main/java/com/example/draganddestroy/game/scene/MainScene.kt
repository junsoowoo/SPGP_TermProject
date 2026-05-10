package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.draganddestroy.game.objects.Bullet
import com.example.draganddestroy.game.objects.DragPathEffect
import com.example.draganddestroy.game.objects.Enemy
import com.example.draganddestroy.game.objects.EnemyGenerator
import com.example.draganddestroy.game.objects.PlayerShip
import com.example.draganddestroy.game.objects.TemporaryTurret
import com.example.draganddestroy.game.util.CollisionHelper
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class Layer {
        BACKGROUND,
        EFFECT,
        PLAYER,
        BULLET,
        ENEMY,
        TURRET,
        UI,
    }

    override val world = World(Layer.entries.toTypedArray())

    private val player = PlayerShip(gctx)
    private val enemyGenerator = EnemyGenerator(gctx, world)

    private var currentDragPath: DragPathEffect? = null

    private val bgPaint = Paint().apply {
        color = Color.rgb(10, 10, 30)
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 36f
        isAntiAlias = true
    }

    override fun onEnter() {
        world.add(BackgroundObject(), Layer.BACKGROUND)
        world.add(player, Layer.PLAYER)
        world.add(enemyGenerator, Layer.UI)
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        player.tryFire()?.let {
            world.add(it, Layer.BULLET)
        }

        checkCollision()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = gctx.metrics.fromScreen(event.x, event.y)
        val x = point.x
        val y = point.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (x < PLAYER_CONTROL_WIDTH) {
                    player.setTargetY(y)
                } else {
                    val path = DragPathEffect(x, y)
                    currentDragPath = path
                    world.add(path, Layer.EFFECT)
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (x < PLAYER_CONTROL_WIDTH) {
                    player.setTargetY(y)
                } else {
                    currentDragPath?.addPoint(x, y)
                }
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val path = currentDragPath

                if (path != null) {
                    world.remove(path, Layer.EFFECT)

                    if (x >= PLAYER_CONTROL_WIDTH) {
                        world.add(
                            TemporaryTurret(gctx, world, x, y, path),
                            Layer.TURRET
                        )
                    }
                }

                currentDragPath = null
                return true
            }
        }

        return true
    }

    private fun checkCollision() {
        val bullets = world.objectsAt(Layer.BULLET)
        val enemies = world.objectsAt(Layer.ENEMY)

        for (bi in bullets.lastIndex downTo 0) {
            val bullet = bullets[bi] as? Bullet ?: continue
            if (bullet.isDead) continue

            for (ei in enemies.lastIndex downTo 0) {
                val enemy = enemies[ei] as? Enemy ?: continue
                if (enemy.isDead) continue

                if (CollisionHelper.circleCollision(
                        bullet.x,
                        bullet.y,
                        bullet.radius,
                        enemy.x,
                        enemy.y,
                        enemy.radius
                    )
                ) {
                    enemy.takeDamage(bullet.damage)
                    bullet.isDead = true

                    world.remove(bullet, Layer.BULLET)

                    if (enemy.isDead) {
                        world.remove(enemy, Layer.ENEMY)
                    }

                    break
                }
            }
        }
    }

    private inner class BackgroundObject : IGameObject {
        override fun update(gctx: GameContext) {
        }

        override fun draw(canvas: Canvas) {
            canvas.drawRect(
                0f,
                0f,
                gctx.metrics.width,
                gctx.metrics.height,
                bgPaint
            )

            canvas.drawText("Drag and Destroy - Week 4 Prototype", 40f, 60f, textPaint)

            val linePaint = Paint().apply {
                color = Color.argb(120, 0, 255, 255)
                strokeWidth = 4f
            }

            canvas.drawLine(
                PLAYER_CONTROL_WIDTH,
                0f,
                PLAYER_CONTROL_WIDTH,
                gctx.metrics.height,
                linePaint
            )
        }
    }

    companion object {
        const val PLAYER_CONTROL_WIDTH = 280f
    }
}