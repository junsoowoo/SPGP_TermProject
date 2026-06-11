package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import com.example.draganddestroy.R
import com.example.draganddestroy.game.data.PickupType
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sin

class PickupItem(
    private val gctx: GameContext,
    startX: Float,
    startY: Float,
    val type: PickupType,
) : IGameObject {

    var x = startX
        private set

    var y = startY
        private set

    val radius = 24f
    var isDead = false

    private val speed = 230f
    private var floatTime = 0f

    private val sprite = Sprite(gctx, if (type == PickupType.MAGNET) R.drawable.item_magnet else R.drawable.item_heal)

    init {
        sprite.setSize(70f, 70f)
        sprite.setCenter(x, y)
    }

    override fun update(gctx: GameContext) {
        floatTime += gctx.frameTime

        x -= speed * gctx.frameTime
        y += sin(floatTime * 5f) * 25f * gctx.frameTime

        if (x < -60f) {
            isDead = true
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.PICKUP)
        }
    }

    override fun draw(canvas: Canvas) {
        sprite.setCenter(x, y)
        sprite.draw(canvas)
    }
}