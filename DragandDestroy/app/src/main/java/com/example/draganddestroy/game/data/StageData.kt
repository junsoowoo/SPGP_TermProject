package com.example.draganddestroy.game.data

data class StageData(
    val stageNumber: Int,
    val title: String,
    val duration: Float,
    val enemyBaseHp: Int,
    val enemyHpIncrease: Int,
    val spawnInterval: Float,
    val coinReward: Int,
    val enemySpeed: Float,
    val enemyRadius: Float,
    val enemyAttackDamage: Int,
    val enemyFireInterval: Float,
    val isBossStage: Boolean,
)