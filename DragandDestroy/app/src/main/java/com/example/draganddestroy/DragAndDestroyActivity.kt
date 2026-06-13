package com.example.draganddestroy

import android.view.KeyEvent
import com.example.draganddestroy.game.data.DebugCommand
import com.example.draganddestroy.game.scene.StageSelectScene
import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.draganddestroy.game.scene.TitleScene

class DragAndDestroyActivity : BaseGameActivity() {

    override val drawsDebugGrid: Boolean = true
    override val drawsDebugInfo: Boolean = true
    override val drawsFpsGraph: Boolean = false

    override fun createRootScene(gctx: GameContext): Scene {
        gctx.metrics.setSize(1600f, 900f)
        return TitleScene(gctx)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_1 -> {
                    DebugCommand.requestStage(1)
                    return true
                }

                KeyEvent.KEYCODE_2 -> {
                    DebugCommand.requestStage(2)
                    return true
                }

                KeyEvent.KEYCODE_3 -> {
                    DebugCommand.requestStage(3)
                    return true
                }

                KeyEvent.KEYCODE_4 -> {
                    DebugCommand.requestStage(4)
                    return true
                }

                KeyEvent.KEYCODE_5 -> {
                    DebugCommand.requestStage(5)
                    return true
                }

                KeyEvent.KEYCODE_6 -> {
                    DebugCommand.requestStore()
                    return true
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onPause() {
        super.onPause()
    }
}