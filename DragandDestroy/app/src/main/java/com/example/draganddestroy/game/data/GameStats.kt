package com.example.draganddestroy.game.data

object GameStats {
    var gold = 0
    var stageGold = 0

    var selectedTurretType = TurretType.BASIC

    var playerDamageLevel = 1
    var playerMaxHpLevel = 1
    var playerCoinGainLevel = 1
    var playerMoveSpeedLevel = 1
    var playerFireRateLevel = 1

    var turretRangeLevel = 1
    var turretCostLevel = 1
    var turretHpLevel = 1
    var turretDamageLevel = 1

    val playerDamage: Int
        get() = 4 + (playerDamageLevel - 1) * 2

    val playerMaxHp: Int
        get() = 100 + (playerMaxHpLevel - 1) * 25

    val playerCoinBonus: Int
        get() = (playerCoinGainLevel - 1) * 2

    val playerMoveSpeed: Float
        get() = 1100f + (playerMoveSpeedLevel - 1) * 90f

    val playerFireInterval: Float
        get() = (0.08f - (playerFireRateLevel - 1) * 0.007f).coerceAtLeast(0.045f)

    val turretRangeBonus: Float
        get() = (turretRangeLevel - 1) * 25f

    val turretInstallCostRate: Float
        get() = (1.0f - (turretCostLevel - 1) * 0.07f).coerceAtLeast(0.55f)

    val turretHpBonus: Int
        get() = (turretHpLevel - 1) * 25

    val turretDamageBonus: Int
        get() = (turretDamageLevel - 1) * 12

    fun getBasicTurretDamage(): Int {
        return 70 + turretDamageBonus
    }

    fun getRapidTurretDamage(): Int {
        return 16 + turretDamageBonus / 2
    }

    fun getBasicTurretHp(): Int {
        return 90 + turretHpBonus
    }

    fun getRapidTurretHp(): Int {
        return 60 + turretHpBonus
    }

    fun getBasicTurretRange(): Float {
        return 280f + turretRangeBonus
    }

    fun getRapidTurretRange(): Float {
        return 230f + turretRangeBonus
    }

    fun getCoinValue(baseValue: Int): Int {
        return baseValue + playerCoinBonus
    }

    fun addGold(amount: Int) {
        gold += amount
        stageGold += amount
    }

    fun resetStageGold() {
        stageGold = 0
    }

    fun getUpgradeCost(type: UpgradeType): Int {
        return when (type) {
            UpgradeType.PLAYER_DAMAGE -> playerDamageLevel * 90
            UpgradeType.PLAYER_MAX_HP -> playerMaxHpLevel * 110
            UpgradeType.PLAYER_COIN_GAIN -> playerCoinGainLevel * 130
            UpgradeType.PLAYER_MOVE_SPEED -> playerMoveSpeedLevel * 100
            UpgradeType.PLAYER_FIRE_RATE -> playerFireRateLevel * 140

            UpgradeType.TURRET_RANGE -> turretRangeLevel * 120
            UpgradeType.TURRET_COST -> turretCostLevel * 150
            UpgradeType.TURRET_HP -> turretHpLevel * 100
            UpgradeType.TURRET_DAMAGE -> turretDamageLevel * 130
        }
    }

    fun getUpgradeLevel(type: UpgradeType): Int {
        return when (type) {
            UpgradeType.PLAYER_DAMAGE -> playerDamageLevel
            UpgradeType.PLAYER_MAX_HP -> playerMaxHpLevel
            UpgradeType.PLAYER_COIN_GAIN -> playerCoinGainLevel
            UpgradeType.PLAYER_MOVE_SPEED -> playerMoveSpeedLevel
            UpgradeType.PLAYER_FIRE_RATE -> playerFireRateLevel

            UpgradeType.TURRET_RANGE -> turretRangeLevel
            UpgradeType.TURRET_COST -> turretCostLevel
            UpgradeType.TURRET_HP -> turretHpLevel
            UpgradeType.TURRET_DAMAGE -> turretDamageLevel
        }
    }

    fun upgrade(type: UpgradeType): Boolean {
        val cost = getUpgradeCost(type)
        if (gold < cost) return false

        gold -= cost

        when (type) {
            UpgradeType.PLAYER_DAMAGE -> playerDamageLevel++
            UpgradeType.PLAYER_MAX_HP -> playerMaxHpLevel++
            UpgradeType.PLAYER_COIN_GAIN -> playerCoinGainLevel++
            UpgradeType.PLAYER_MOVE_SPEED -> playerMoveSpeedLevel++
            UpgradeType.PLAYER_FIRE_RATE -> playerFireRateLevel++

            UpgradeType.TURRET_RANGE -> turretRangeLevel++
            UpgradeType.TURRET_COST -> turretCostLevel++
            UpgradeType.TURRET_HP -> turretHpLevel++
            UpgradeType.TURRET_DAMAGE -> turretDamageLevel++
        }

        return true
    }

    fun resetAll() {
        gold = 0
        stageGold = 0
        selectedTurretType = TurretType.BASIC

        playerDamageLevel = 1
        playerMaxHpLevel = 1
        playerCoinGainLevel = 1
        playerMoveSpeedLevel = 1
        playerFireRateLevel = 1

        turretRangeLevel = 1
        turretCostLevel = 1
        turretHpLevel = 1
        turretDamageLevel = 1
    }
}