package com.example.draganddestroy

import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class DragAndDestroyActivity : BaseGameActivity() {

    override val drawsDebugGrid: Boolean = true
    override val drawsDebugInfo: Boolean = true
    override val drawsFpsGraph: Boolean = false

    override fun createRootScene(gctx: GameContext): Scene {
        gctx.metrics.setSize(1600f, 900f)
        return MainScene(gctx)
    }
}