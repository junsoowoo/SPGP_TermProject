package com.example.draganddestroy.game.data

object StageManager {
    private var currentStageIndex = 0

    private val stages = listOf(
        StageData(stageNumber = 1, title = "Stage 1", duration = 60f, enemyBaseHp = 52, enemyHpIncrease = 7, spawnInterval = 1.22f, coinReward = 18, enemySpeed = 165f, enemyRadius = 45f, enemyAttackDamage = 5, enemyFireInterval = 2.6f, isBossStage = false),
        StageData(stageNumber = 2, title = "Stage 2", duration = 60f, enemyBaseHp = 80, enemyHpIncrease = 11, spawnInterval = 1.08f, coinReward = 25, enemySpeed = 185f, enemyRadius = 48f, enemyAttackDamage = 7, enemyFireInterval = 2.3f, isBossStage = false),
        StageData(stageNumber = 3, title = "Stage 3", duration = 60f, enemyBaseHp = 118, enemyHpIncrease = 16, spawnInterval = 0.98f, coinReward = 34, enemySpeed = 205f, enemyRadius = 50f, enemyAttackDamage = 9, enemyFireInterval = 2.05f, isBossStage = false),
        StageData(stageNumber = 4, title = "Stage 4", duration = 60f, enemyBaseHp = 160, enemyHpIncrease = 23, spawnInterval = 0.88f, coinReward = 46, enemySpeed = 225f, enemyRadius = 52f, enemyAttackDamage = 11, enemyFireInterval = 1.85f, isBossStage = false),
        StageData(stageNumber = 5, title = "Boss Stage", duration = 60f, enemyBaseHp = 1250, enemyHpIncrease = 0, spawnInterval = 999f, coinReward = 380, enemySpeed = 78f, enemyRadius = 95f, enemyAttackDamage = 18, enemyFireInterval = 1.5f, isBossStage = true),
    )

    val currentStage: StageData
        get() = stages[currentStageIndex]

    val isLastStage: Boolean
        get() = currentStageIndex >= stages.lastIndex

    fun goNextStage() {
        if (currentStageIndex < stages.lastIndex) {
            currentStageIndex++
        }
    }

    fun setStageNumber(stageNumber: Int) {
        currentStageIndex = (stageNumber - 1).coerceIn(0, stages.lastIndex)
    }

    fun getNormalStageForBossMinion(index: Int): StageData {
        return stages[index % 4]
    }

    fun reset() {
        currentStageIndex = 0
    }
}