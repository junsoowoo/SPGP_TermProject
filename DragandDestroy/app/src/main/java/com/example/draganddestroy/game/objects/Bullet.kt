package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Color
import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt

class Bullet(
    private val gctx: GameContext,
    startX: Float,
    startY: Float,
    val damage: Int,
    private val color: Int = Color.YELLOW,
    dirX: Float = 1f,
    dirY: Float = 0f,
    private val speed: Float = 1300f,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 10f
    var isDead = false

    private var vx = 1f
    private var vy = 0f

    private val imageResId = when (color) {
        Color.MAGENTA -> R.drawable.bullet_turret_basic
        Color.rgb(60, 220, 255) -> R.drawable.bullet_turret_rapid
        else -> R.drawable.bullet_player
    }

    private val sprite = Sprite(gctx, imageResId)

    init {
        setDirection(dirX, dirY)
        sprite.setSize(54f, 24f)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        x += vx * speed * gctx.frameTime
        y += vy * speed * gctx.frameTime

        if (x < -100f || x > gctx.metrics.width + 100f || y < -100f || y > gctx.metrics.height + 100f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.BULLET)
        }
    }

    override fun draw(canvas: Canvas) {
        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }

    private fun setDirection(dirX: Float, dirY: Float) {
        val length = sqrt(dirX * dirX + dirY * dirY)

        if (length <= 0.0001f) {
            vx = 1f
            vy = 0f
            return
        }

        vx = dirX / length
        vy = dirY / length
    }
}