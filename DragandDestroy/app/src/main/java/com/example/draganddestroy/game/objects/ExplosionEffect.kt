package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import android.graphics.Rect
import com.example.draganddestroy.R
import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class ExplosionEffect(
    gctx: GameContext,
    x: Float,
    y: Float,
) : Sprite(gctx, R.drawable.explosion_strip) {

    private var elapsedTime = 0f
    private var lifeTime = FRAME_COUNT / FPS
    private val frameRect = Rect()

    init {
        setCenter(x, y)
        setSize(135f, 135f)
    }

    override fun update(gctx: GameContext) {
        elapsedTime += gctx.frameTime
        lifeTime -= gctx.frameTime

        if (lifeTime <= 0f) {
            val scene = gctx.scene as? MainScene
            scene?.world?.remove(this, MainScene.Layer.EFFECT)
        }
    }

    override fun draw(canvas: Canvas) {
        val frameIndex = (elapsedTime * FPS).toInt().coerceIn(0, FRAME_COUNT - 1)
        val frameWidth = bitmapWidth / FRAME_COUNT

        frameRect.set(
            frameIndex * frameWidth,
            0,
            if (frameIndex == FRAME_COUNT - 1) bitmapWidth else (frameIndex + 1) * frameWidth,
            bitmapHeight,
        )

        canvas.drawBitmap(bitmap, frameRect, dstRect, null)
    }

    companion object {
        private const val FPS = 18f
        private const val FRAME_COUNT = 10
    }
}