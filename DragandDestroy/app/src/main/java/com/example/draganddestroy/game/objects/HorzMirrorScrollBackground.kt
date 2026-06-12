package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.floor

class HorzMirrorScrollBackground(
    gctx: GameContext,
    resId: Int,
    private val speed: Float,
) : Sprite(gctx, resId) {

    private val screenWidth = gctx.metrics.width
    private val screenHeight = gctx.metrics.height
    private var scrollX = 0f

    init {
        setSize(screenWidth, screenHeight)
    }

    override fun update(gctx: GameContext) {
        scrollX += speed * gctx.frameTime
    }

    override fun draw(canvas: Canvas) {
        val startTile = floor(scrollX / screenWidth).toInt() - 1

        for (i in 0 until 4) {
            val tileIndex = startTile + i
            val left = tileIndex * screenWidth - scrollX
            val right = left + screenWidth

            dstRect.set(left, 0f, right, screenHeight)

            if (tileIndex % 2 == 0) {
                canvas.drawBitmap(bitmap, null, dstRect, null)
            } else {
                canvas.save()
                canvas.scale(-1f, 1f, dstRect.centerX(), dstRect.centerY())
                canvas.drawBitmap(bitmap, null, dstRect, null)
                canvas.restore()
            }
        }
    }
}