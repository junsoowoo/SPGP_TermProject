package com.example.draganddestroy.game.objects

import android.graphics.Canvas
import com.example.draganddestroy.game.data.EnemyType
import com.example.draganddestroy.game.data.StageData
import com.example.draganddestroy.game.data.StageManager
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
    private var spawnCount = 0

    private var bossSpawned = false
    private var bossMinionTimer = 0f
    private var bossMinionCount = 0

    override fun update(gctx: GameContext) {
        val stage = StageManager.currentStage

        if (stage.isBossStage) {
            updateBossStage(gctx)
            return
        }

        updateNormalStage(gctx, stage)
    }

    override fun draw(canvas: Canvas) {
    }

    private fun updateNormalStage(gctx: GameContext, stage: StageData) {
        spawnTimer += gctx.frameTime

        if (spawnTimer >= stage.spawnInterval) {
            spawnTimer = 0f
            spawnEnemyByType(stage, spawnCount, pickEnemyType())
            spawnCount++
        }
    }

    private fun updateBossStage(gctx: GameContext) {
        if (!bossSpawned) {
            spawnBoss()
            bossSpawned = true
        }

        bossMinionTimer += gctx.frameTime

        if (bossMinionTimer >= BOSS_MINION_SPAWN_INTERVAL) {
            bossMinionTimer = 0f
            val minionStage = StageManager.getNormalStageForBossMinion(bossMinionCount)
            spawnEnemyByType(minionStage, bossMinionCount, pickEnemyType())
            bossMinionCount++
        }
    }

    private fun pickEnemyType(): EnemyType {
        val r = Random.nextInt(100)

        return when {
            r < 50 -> EnemyType.NORMAL
            r < 78 -> EnemyType.FAST
            else -> EnemyType.TANK
        }
    }

    private fun spawnEnemyByType(stage: StageData, index: Int, type: EnemyType) {
        val baseHp = stage.enemyBaseHp + index / 4 * stage.enemyHpIncrease

        val hp = when (type) {
            EnemyType.NORMAL -> baseHp
            EnemyType.FAST -> (baseHp * 0.65f).toInt()
            EnemyType.TANK -> (baseHp * 2.2f).toInt()
        }

        val enemySpeed = when (type) {
            EnemyType.NORMAL -> stage.enemySpeed
            EnemyType.FAST -> stage.enemySpeed * 1.55f
            EnemyType.TANK -> stage.enemySpeed * 0.65f
        }

        val enemyRadius = when (type) {
            EnemyType.NORMAL -> stage.enemyRadius
            EnemyType.FAST -> stage.enemyRadius * 0.75f
            EnemyType.TANK -> stage.enemyRadius * 1.35f
        }

        val damage = when (type) {
            EnemyType.NORMAL -> stage.enemyAttackDamage
            EnemyType.FAST -> (stage.enemyAttackDamage * 0.8f).toInt().coerceAtLeast(1)
            EnemyType.TANK -> (stage.enemyAttackDamage * 1.4f).toInt()
        }

        val fireInterval = when (type) {
            EnemyType.NORMAL -> stage.enemyFireInterval
            EnemyType.FAST -> stage.enemyFireInterval * 1.25f
            EnemyType.TANK -> stage.enemyFireInterval * 0.85f
        }

        val reward = when (type) {
            EnemyType.NORMAL -> stage.coinReward
            EnemyType.FAST -> (stage.coinReward * 0.8f).toInt()
            EnemyType.TANK -> (stage.coinReward * 1.8f).toInt()
        }

        val startX = gctx.metrics.width + enemyRadius + 30f
        val startY = Random.nextInt(100, (gctx.metrics.height - 100f).toInt()).toFloat()

        val enemy = Enemy(startX = startX, startY = startY, hp = hp, maxHp = hp, rewardGold = reward, radius = enemyRadius, speed = enemySpeed, isBoss = false, attackDamage = damage, fireInterval = fireInterval, enemyType = type)
        world.add(enemy, MainScene.Layer.ENEMY)
    }

    private fun spawnBoss() {
        val stage = StageManager.currentStage

        val startX = gctx.metrics.width + stage.enemyRadius + 120f
        val startY = gctx.metrics.height * 0.5f

        val boss = BossEnemy(startX = startX, startY = startY, hp = stage.enemyBaseHp, rewardGold = stage.coinReward, radius = stage.enemyRadius, speed = stage.enemySpeed, attackDamage = stage.enemyAttackDamage, fireInterval = stage.enemyFireInterval)
        world.add(boss, MainScene.Layer.ENEMY)
    }

    companion object {
        private const val BOSS_MINION_SPAWN_INTERVAL = 2.4f
    }
}