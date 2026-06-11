package com.example.draganddestroy.game.objects

import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class HitEffect(
    private val gctx: GameContext,
    x: Float,
    y: Float,
) : Sprite(gctx, R.drawable.hit_effect) {

    private var lifeTime = 0.16f

    init {
        setCenter(x, y)
        setSize(82f, 82f)
    }

    override fun update(gctx: GameContext) {
        lifeTime -= gctx.frameTime

        if (lifeTime <= 0f) {
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.EFFECT)
        }
    }
}