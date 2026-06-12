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
        get() = 135 + (playerMaxHpLevel - 1) * 35

    val playerCoinBonus: Int
        get() = (playerCoinGainLevel - 1) * 3

    val playerMoveSpeed: Float
        get() = 1100f + (playerMoveSpeedLevel - 1) * 90f

    val playerFireInterval: Float
        get() = (0.08f - (playerFireRateLevel - 1) * 0.007f).coerceAtLeast(0.045f)

    val turretRangeBonus: Float
        get() = (turretRangeLevel - 1) * 25f

    val turretInstallCostRate: Float
        get() = (1.0f - (turretCostLevel - 1) * 0.08f).coerceAtLeast(0.50f)

    val turretHpBonus: Int
        get() = (turretHpLevel - 1) * 35

    val turretDamageBonus: Int
        get() = (turretDamageLevel - 1) * 12

    fun getBasicTurretDamage(): Int {
        return 72 + turretDamageBonus
    }

    fun getRapidTurretDamage(): Int {
        return 17 + turretDamageBonus / 2
    }

    fun getBasicTurretHp(): Int {
        return 125 + turretHpBonus
    }

    fun getRapidTurretHp(): Int {
        return 85 + turretHpBonus
    }

    fun getBasicTurretRange(): Float {
        return 290f + turretRangeBonus
    }

    fun getRapidTurretRange(): Float {
        return 240f + turretRangeBonus
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
            UpgradeType.PLAYER_DAMAGE -> playerDamageLevel * 65
            UpgradeType.PLAYER_MAX_HP -> playerMaxHpLevel * 70
            UpgradeType.PLAYER_COIN_GAIN -> playerCoinGainLevel * 85
            UpgradeType.PLAYER_MOVE_SPEED -> playerMoveSpeedLevel * 75
            UpgradeType.PLAYER_FIRE_RATE -> playerFireRateLevel * 95

            UpgradeType.TURRET_RANGE -> turretRangeLevel * 80
            UpgradeType.TURRET_COST -> turretCostLevel * 95
            UpgradeType.TURRET_HP -> turretHpLevel * 70
            UpgradeType.TURRET_DAMAGE -> turretDamageLevel * 90
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