package com.example.draganddestroy.game.data

object StageManager {
    private var currentStageIndex = 0

    private val stages = listOf(
        StageData(stageNumber = 1, title = "Stage 1", duration = 60f, enemyBaseHp = 55, enemyHpIncrease = 8, spawnInterval = 1.15f, coinReward = 12, enemySpeed = 170f, enemyRadius = 45f, enemyAttackDamage = 7, enemyFireInterval = 2.4f, isBossStage = false),
        StageData(stageNumber = 2, title = "Stage 2", duration = 60f, enemyBaseHp = 85, enemyHpIncrease = 12, spawnInterval = 1.0f, coinReward = 18, enemySpeed = 190f, enemyRadius = 48f, enemyAttackDamage = 9, enemyFireInterval = 2.1f, isBossStage = false),
        StageData(stageNumber = 3, title = "Stage 3", duration = 60f, enemyBaseHp = 125, enemyHpIncrease = 18, spawnInterval = 0.9f, coinReward = 26, enemySpeed = 210f, enemyRadius = 50f, enemyAttackDamage = 11, enemyFireInterval = 1.9f, isBossStage = false),
        StageData(stageNumber = 4, title = "Stage 4", duration = 60f, enemyBaseHp = 170, enemyHpIncrease = 25, spawnInterval = 0.8f, coinReward = 36, enemySpeed = 230f, enemyRadius = 52f, enemyAttackDamage = 14, enemyFireInterval = 1.7f, isBossStage = false),
        StageData(stageNumber = 5, title = "Boss Stage", duration = 60f, enemyBaseHp = 1400, enemyHpIncrease = 0, spawnInterval = 999f, coinReward = 300, enemySpeed = 80f, enemyRadius = 95f, enemyAttackDamage = 22, enemyFireInterval = 1.3f, isBossStage = true),
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