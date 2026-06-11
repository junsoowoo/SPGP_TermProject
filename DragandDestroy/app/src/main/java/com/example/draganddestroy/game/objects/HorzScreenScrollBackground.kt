package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class HorzScreenScrollBackground(
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
        var curr = scrollX % screenWidth

        if (curr > 0f) {
            curr -= screenWidth
        }

        while (curr < screenWidth) {
            dstRect.set(curr, 0f, curr + screenWidth, screenHeight)
            canvas.drawBitmap(bitmap, null, dstRect, null)
            curr += screenWidth
        }
    }
}