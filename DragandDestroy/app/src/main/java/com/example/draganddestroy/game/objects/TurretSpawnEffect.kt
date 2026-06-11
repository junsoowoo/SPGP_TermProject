package com.example.draganddestroy.game.objects

import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class TurretSpawnEffect(
    private val gctx: GameContext,
    x: Float,
    y: Float,
) : Sprite(gctx, R.drawable.turret_spawn_effect) {

    private var lifeTime = 0.22f

    init {
        setCenter(x, y)
        setSize(120f, 120f)
    }

    override fun update(gctx: GameContext) {
        lifeTime -= gctx.frameTime

        if (lifeTime <= 0f) {
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.EFFECT)
        }
    }
}