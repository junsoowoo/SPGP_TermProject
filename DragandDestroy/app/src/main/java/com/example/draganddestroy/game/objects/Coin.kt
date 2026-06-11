package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sin
import kotlin.math.sqrt

class Coin(
    private val gctx: GameContext,
    startX: Float,
    startY: Float,
    private val baseValue: Int,
    private val large: Boolean,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val value: Int
        get() = GameStats.getCoinValue(baseValue)

    val radius = if (large) 28f else 17f
    var isDead = false

    private val speed = if (large) 210f else 260f
    private var floatTime = 0f

    private val sprite = Sprite(gctx, if (large) R.drawable.coin_large else R.drawable.coin_small)

    init {
        val size = if (large) 70f else 46f
        sprite.setSize(size, size)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        floatTime += gctx.frameTime

        x -= speed * gctx.frameTime
        y += sin(floatTime * 6f) * 20f * gctx.frameTime

        if (x < -50f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.COIN)
        }
    }

    override fun draw(canvas: Canvas) {
        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }

    fun moveToward(targetX: Float, targetY: Float, frameTime: Float) {
        val dx = targetX - x
        val dy = targetY - y
        val length = sqrt(dx * dx + dy * dy)

        if (length <= 0.001f) return

        val magnetSpeed = 720f
        x += dx / length * magnetSpeed * frameTime
        y += dy / length * magnetSpeed * frameTime
    }
}