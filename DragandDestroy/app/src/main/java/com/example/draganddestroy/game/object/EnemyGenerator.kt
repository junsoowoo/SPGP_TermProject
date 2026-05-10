package com.example.draganddestroy.game.objects

import com.example.draganddestroy.game.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.random.Random

class EnemyGenerator(
    private val gctx: GameContext,
    private val world: World<MainScene.Layer>,
) : IGameObject {

    private var spawnTimer = 0f
    private val spawnInterval = 1.1f

    private var waveCount = 0

    override fun update(gctx: GameContext) {
        spawnTimer += gctx.frameTime

        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f
            spawnEnemy()
        }
    }

    override fun draw(canvas: android.graphics.Canvas) {
    }

    private fun spawnEnemy() {
        waveCount++

        val startX = gctx.metrics.width + 80f
        val startY = Random.nextInt(100, (gctx.metrics.height - 100f).toInt()).toFloat()

        val hp = 50 + waveCount / 5 * 10

        val enemy = Enemy(
            startX = startX,
            startY = startY,
            hp = hp
        )

        world.add(enemy, MainScene.Layer.ENEMY)
    }
}