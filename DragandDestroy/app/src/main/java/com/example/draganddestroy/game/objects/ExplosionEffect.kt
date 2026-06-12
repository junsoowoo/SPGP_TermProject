package com.example.draganddestroy.game.objects

import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.AnimSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class ExplosionEffect(
    gctx: GameContext,
    x: Float,
    y: Float,
) : AnimSprite(gctx, R.drawable.explosion_strip, FPS, FRAME_COUNT) {

    private var lifeTime = FRAME_COUNT / FPS

    init {
        setCenter(x, y)
        setSize(150f, 150f)
    }

    override fun update(gctx: GameContext) {
        lifeTime -= gctx.frameTime

        if (lifeTime <= 0f) {
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.EFFECT)
        }
    }

    companion object {
        private const val FPS = 18f
        private const val FRAME_COUNT = 10
    }
}