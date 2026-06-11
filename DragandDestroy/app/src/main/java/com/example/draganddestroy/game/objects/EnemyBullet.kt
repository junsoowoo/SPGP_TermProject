package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt

class EnemyBullet(
    private val gctx: GameContext,
    startX: Float,
    startY: Float,
    dirX: Float,
    dirY: Float,
    val damage: Int,
    private val speed: Float = 520f,
    val radius: Float = 12f,
    private val color: Int = Color.rgb(255, 90, 60),
    private val bossBullet: Boolean = false,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    var isDead = false

    private var vx = -1f
    private var vy = 0f

    private val sprite = Sprite(gctx, if (bossBullet) R.drawable.bullet_boss else R.drawable.bullet_enemy)

    init {
        val length = sqrt(dirX * dirX + dirY * dirY)

        if (length > 0.0001f) {
            vx = dirX / length
            vy = dirY / length
        }

        val size = if (bossBullet) radius * 5.0f else radius * 4.0f
        sprite.setSize(size, size * 0.45f)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        x += vx * speed * gctx.frameTime
        y += vy * speed * gctx.frameTime

        if (x < -120f || x > gctx.metrics.width + 120f || y < -120f || y > gctx.metrics.height + 120f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.ENEMY_BULLET)
        }
    }

    override fun draw(canvas: Canvas) {
        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }
}