package com.example.draganddestroy.game.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.draganddestroy.game.data.DebugCommand
import com.example.draganddestroy.game.data.GameStats
import com.example.draganddestroy.game.data.PickupType
import com.example.draganddestroy.game.data.StageManager
import com.example.draganddestroy.game.objects.BossEnemy
import com.example.draganddestroy.game.objects.Bullet
import com.example.draganddestroy.game.objects.Coin
import com.example.draganddestroy.game.objects.DragPathEffect
import com.example.draganddestroy.game.objects.Enemy
import com.example.draganddestroy.game.objects.EnemyBullet
import com.example.draganddestroy.game.objects.EnemyGenerator
import com.example.draganddestroy.game.objects.PickupItem
import com.example.draganddestroy.game.objects.PlayerShip
import com.example.draganddestroy.game.objects.TemporaryTurret
import com.example.draganddestroy.game.util.CollisionHelper
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.util.Gauge
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt
import kotlin.random.Random
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import com.example.draganddestroy.game.objects.ExplosionEffect
import com.example.draganddestroy.game.util.GameSound
import com.example.draganddestroy.game.objects.HitEffect
import com.example.draganddestroy.game.objects.TurretSpawnEffect
import com.example.draganddestroy.game.objects.HorzMirrorScrollBackground

class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class Layer {
        BACKGROUND,
        EFFECT,
        PLAYER,
        BULLET,
        ENEMY_BULLET,
        ENEMY,
        COIN,
        PICKUP,
        TURRET,
        UI,
    }

    private enum class TouchMode {
        NONE,
        TURRET,
    }

    override val world = World(enumValues<Layer>())

    private val player = PlayerShip(gctx)
    private val enemyGenerator = EnemyGenerator(gctx, world)

    private val pauseButton = RectF(1450f, 16f, 1570f, 76f)

    private var touchMode = TouchMode.NONE
    private var currentDragPath: DragPathEffect? = null

    private var turretPointerId = INVALID_POINTER_ID
    private var joystickPointerId = INVALID_POINTER_ID

    private var lastDragX = 0f
    private var lastDragY = 0f
    private var dragDistanceSinceLastTurret = 0f

    private var joystickActive = false
    private var joystickThumbX = 0f
    private var joystickThumbY = 0f
    private var joystickDirX = 0f
    private var joystickDirY = 0f
    private var joystickPower = 0f

    private var turretEnergy = MAX_TURRET_ENERGY
    private var killCount = 0
    private var stageTimeLeft = StageManager.currentStage.duration
    private var elapsedTime = 0f
    private var nextShopElapsedTime = MID_STAGE_SHOP_INTERVAL
    private var stageEndHandled = false
    private var bossKilled = false

    private var magnetTime = 0f

    private val turretGauge = Gauge(thickness = 0.14f, fgColor = Color.CYAN, bgColor = Color.DKGRAY)
    private val playerHpGauge = Gauge(thickness = 0.14f, fgColor = Color.GREEN, bgColor = Color.DKGRAY)
    private val magnetGauge = Gauge(thickness = 0.12f, fgColor = Color.rgb(70, 160, 255), bgColor = Color.DKGRAY)

    private var stageMessageText = ""
    private var stageMessageSubText = ""
    private var stageMessageTime = 0f

    private var installMessageText = ""
    private var installMessageTime = 0f

    private var pendingStageEndSuccess = false
    private var stageTransitionDelay = 0f

    override fun onEnter() {
        GameStats.resetStageGold()
        resetJoystick()

        GameSound.startBgm(gctx)

        val stage = StageManager.currentStage
        showStageMessage(
            if (stage.isBossStage) "BOSS STAGE" else "STAGE ${stage.stageNumber} START",
            stage.title,
            1.35f
        )
        GameSound.playStageStart(gctx)

        world.add(BackgroundObject(), Layer.BACKGROUND)
        world.add(player, Layer.PLAYER)
        world.add(enemyGenerator, Layer.UI)
        world.add(HudObject(), Layer.UI)
    }

    override fun update(gctx: GameContext) {
        DebugCommand.consumeStageRequest()?.let {
            StageManager.setStageNumber(it)
            gctx.sceneStack.change(MainScene(gctx))
            return
        }

        if (DebugCommand.consumeStoreRequest()) {
            gctx.sceneStack.push(StoreScene(gctx, inStageShop = true))
            return
        }

        updateUiMessages(gctx)

        if (stageEndHandled) {
            stageTransitionDelay -= gctx.frameTime
            if (stageTransitionDelay <= 0f) {
                moveToNextSceneAfterStageEnd()
            }
            return
        }

        stageTimeLeft -= gctx.frameTime
        elapsedTime += gctx.frameTime

        if (stageTimeLeft < 0f) stageTimeLeft = 0f

        if (magnetTime > 0f) {
            magnetTime -= gctx.frameTime
            if (magnetTime < 0f) magnetTime = 0f
        }

        if (shouldOpenMidStageShop()) {
            openMidStageShop()
            return
        }

        if (touchMode != TouchMode.TURRET) {
            turretEnergy = (turretEnergy + TURRET_ENERGY_RECOVER_PER_SEC * gctx.frameTime).coerceAtMost(MAX_TURRET_ENERGY)
        }

        applyMovementInput()

        super.update(gctx)

        player.tryFire()?.let {
            world.add(it, Layer.BULLET)
        }

        if (magnetTime > 0f) {
            applyMagnetEffect(gctx)
        }

        makeEnemiesFire()
        checkBulletEnemyCollision()
        checkEnemyBulletTurretCollision()
        checkEnemyBulletPlayerCollision()
        checkPlayerCoinCollision()
        checkPlayerPickupCollision()
        checkEnemyPlayerCollision()
        checkStageEnd()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (stageEndHandled) return true

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)
                val point = gctx.metrics.fromScreen(event.getX(index), event.getY(index))
                val x = point.x
                val y = point.y

                if (pauseButton.contains(x, y)) {
                    GameSound.playButtonClick(gctx)
                    gctx.sceneStack.push(PauseScene(gctx))
                    return true
                }

                if (isInsideJoystickArea(x, y) && joystickPointerId == INVALID_POINTER_ID) {
                    joystickPointerId = pointerId
                    startJoystick(x, y)
                    return true
                }

                if (turretPointerId == INVALID_POINTER_ID) {
                    turretPointerId = pointerId
                    touchMode = TouchMode.TURRET
                    startTurretDrag(x, y)
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val pointerId = event.getPointerId(i)
                    val point = gctx.metrics.fromScreen(event.getX(i), event.getY(i))
                    val x = point.x
                    val y = point.y

                    when (pointerId) {
                        joystickPointerId -> updateJoystick(x, y)

                        turretPointerId -> {
                            currentDragPath?.addPoint(x, y)
                            placeTurretsAlongSegment(lastDragX, lastDragY, x, y)
                            lastDragX = x
                            lastDragY = y
                        }
                    }
                }

                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)

                if (pointerId == joystickPointerId) {
                    joystickPointerId = INVALID_POINTER_ID
                    resetJoystick()
                }

                if (pointerId == turretPointerId || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    turretPointerId = INVALID_POINTER_ID
                    finishTurretDrag()
                    touchMode = TouchMode.NONE
                }

                return true
            }
        }

        return true
    }

    private fun shouldOpenMidStageShop(): Boolean {
        val stage = StageManager.currentStage

        if (stage.isBossStage) return false
        if (stageTimeLeft <= 0f) return false
        if (nextShopElapsedTime >= stage.duration) return false

        return elapsedTime >= nextShopElapsedTime
    }

    private fun openMidStageShop() {
        nextShopElapsedTime += MID_STAGE_SHOP_INTERVAL
        finishTurretDrag()
        touchMode = TouchMode.NONE
        turretPointerId = INVALID_POINTER_ID
        gctx.sceneStack.push(StoreScene(gctx, inStageShop = true))
    }

    private fun startJoystick(x: Float, y: Float) {
        joystickActive = true
        updateJoystick(x, y)
    }

    private fun updateJoystick(x: Float, y: Float) {
        val centerX = getJoystickCenterX()
        val centerY = getJoystickCenterY()

        val dx = x - centerX
        val dy = y - centerY
        val distance = sqrt(dx * dx + dy * dy)

        if (distance <= 0.001f) {
            joystickDirX = 0f
            joystickDirY = 0f
            joystickPower = 0f
            joystickThumbX = centerX
            joystickThumbY = centerY
            player.setMoveInput(0f, 0f, 0f)
            return
        }

        val clampedDistance = distance.coerceAtMost(JOYSTICK_MAX_DISTANCE)
        joystickDirX = dx / distance
        joystickDirY = dy / distance
        joystickPower = 1f

        joystickThumbX = centerX + joystickDirX * clampedDistance
        joystickThumbY = centerY + joystickDirY * clampedDistance
    }

    private fun resetJoystick() {
        joystickActive = false
        joystickDirX = 0f
        joystickDirY = 0f
        joystickPower = 0f
        joystickThumbX = getJoystickCenterX()
        joystickThumbY = getJoystickCenterY()
        player.setMoveInput(0f, 0f, 0f)
    }

    private fun applyMovementInput() {
        if (joystickPointerId == INVALID_POINTER_ID) {
            joystickActive = false
            joystickDirX = 0f
            joystickDirY = 0f
            joystickPower = 0f
            joystickThumbX = getJoystickCenterX()
            joystickThumbY = getJoystickCenterY()
            player.setMoveInput(0f, 0f, 0f)
            return
        }

        player.setMoveInput(joystickDirX, joystickDirY, joystickPower)
    }

    private fun isInsideJoystickArea(x: Float, y: Float): Boolean {
        val dx = x - getJoystickCenterX()
        val dy = y - getJoystickCenterY()
        return dx * dx + dy * dy <= JOYSTICK_TOUCH_RADIUS * JOYSTICK_TOUCH_RADIUS
    }

    private fun getJoystickCenterX(): Float {
        return 145f
    }

    private fun getJoystickCenterY(): Float {
        return gctx.metrics.height - 145f
    }

    private fun startTurretDrag(x: Float, y: Float) {
        if (isInsideJoystickArea(x, y)) return

        val path = DragPathEffect(x, y)
        currentDragPath = path
        world.add(path, Layer.EFFECT)

        lastDragX = x
        lastDragY = y
        dragDistanceSinceLastTurret = 0f

        tryInstallTurretAt(x, y)
    }

    private fun finishTurretDrag() {
        currentDragPath?.let {
            world.remove(it, Layer.EFFECT)
        }

        currentDragPath = null
        dragDistanceSinceLastTurret = 0f
    }

    private fun placeTurretsAlongSegment(fromX: Float, fromY: Float, toX: Float, toY: Float) {
        if (isInsideJoystickArea(toX, toY)) return

        val dx = toX - fromX
        val dy = toY - fromY
        val distance = sqrt(dx * dx + dy * dy)

        if (distance <= 0f) return

        val dirX = dx / distance
        val dirY = dy / distance

        var remainDistance = distance
        var currentX = fromX
        var currentY = fromY

        while (dragDistanceSinceLastTurret + remainDistance >= TURRET_SPACING) {
            val needDistance = TURRET_SPACING - dragDistanceSinceLastTurret
            val installX = currentX + dirX * needDistance
            val installY = currentY + dirY * needDistance

            val installed = tryInstallTurretAt(installX, installY)

            if (!installed) {
                dragDistanceSinceLastTurret = 0f
                return
            }

            currentX = installX
            currentY = installY
            remainDistance -= needDistance
            dragDistanceSinceLastTurret = 0f
        }

        dragDistanceSinceLastTurret += remainDistance
    }

    private fun tryInstallTurretAt(x: Float, y: Float): Boolean {
        if (isInsideJoystickArea(x, y)) return false

        val cost = getCurrentTurretCost()
        if (turretEnergy < cost) {
            showInstallMessage()
            return false
        }

        val installX = x.coerceIn(TURRET_RADIUS, gctx.metrics.width - TURRET_RADIUS)
        val installY = y.coerceIn(TURRET_RADIUS, gctx.metrics.height - TURRET_RADIUS)

        world.add(TurretSpawnEffect(gctx, installX, installY), Layer.EFFECT)
        world.add(TemporaryTurret(gctx, world, installX, installY, GameStats.selectedTurretType), Layer.TURRET)

        turretEnergy -= cost
        if (turretEnergy < 0f) turretEnergy = 0f

        return true
    }

    private fun getCurrentTurretCost(): Float {
        return BASE_TURRET_COST * GameStats.turretInstallCostRate
    }

    private fun makeEnemiesFire() {
        val enemies = world.objectsAt(Layer.ENEMY)

        for (i in enemies.indices) {
            val enemy = enemies[i] as? Enemy ?: continue

            if (enemy is BossEnemy) {
                val bullets = enemy.firePatterns(player.x, player.y)
                for (bullet in bullets) {
                    world.add(bullet, Layer.ENEMY_BULLET)
                }
            } else {
                val bullet = enemy.tryFireAt(player.x, player.y) ?: continue
                world.add(bullet, Layer.ENEMY_BULLET)
            }
        }
    }

    private fun checkBulletEnemyCollision() {
        val bullets = world.objectsAt(Layer.BULLET)
        val enemies = world.objectsAt(Layer.ENEMY)

        for (bi in bullets.lastIndex downTo 0) {
            val bullet = bullets[bi] as? Bullet ?: continue
            if (bullet.isDead) continue

            for (ei in enemies.lastIndex downTo 0) {
                val enemy = enemies[ei] as? Enemy ?: continue
                if (enemy.isDead) continue

                if (CollisionHelper.circleCollision(bullet.x, bullet.y, bullet.radius, enemy.x, enemy.y, enemy.radius)) {
                    world.add(HitEffect(gctx, bullet.x, bullet.y), Layer.EFFECT)

                    enemy.takeDamage(bullet.damage)
                    GameSound.playEnemyHit(gctx)
                    bullet.isDead = true
                    world.remove(bullet, Layer.BULLET)

                    if (enemy.isDead) {
                        killCount++

                        if (enemy is BossEnemy) {
                            bossKilled = true
                        }

                        world.add(ExplosionEffect(gctx, enemy.x, enemy.y), Layer.EFFECT)
                        GameSound.playExplosion(gctx)
                        dropRewards(enemy.x, enemy.y, enemy.rewardGold)
                        world.remove(enemy, Layer.ENEMY)
                    }

                    break
                }
            }
        }
    }

    private fun dropRewards(x: Float, y: Float, baseGold: Int) {
        val largeCoin = Random.nextInt(100) < LARGE_COIN_DROP_RATE
        val coinValue = if (largeCoin) baseGold * 3 else baseGold
        world.add(Coin(gctx, x, y, coinValue, largeCoin), Layer.COIN)

        val itemRoll = Random.nextInt(100)

        when {
            itemRoll < MAGNET_DROP_RATE -> {
                world.add(PickupItem(gctx, x + 25f, y - 25f, PickupType.MAGNET), Layer.PICKUP)
            }

            itemRoll < MAGNET_DROP_RATE + HEAL_DROP_RATE -> {
                world.add(PickupItem(gctx, x + 25f, y - 25f, PickupType.HEAL), Layer.PICKUP)
            }
        }
    }

    private fun checkEnemyBulletTurretCollision() {
        val bullets = world.objectsAt(Layer.ENEMY_BULLET)
        val turrets = world.objectsAt(Layer.TURRET)

        for (bi in bullets.lastIndex downTo 0) {
            val bullet = bullets[bi] as? EnemyBullet ?: continue
            if (bullet.isDead) continue

            for (ti in turrets.lastIndex downTo 0) {
                val turret = turrets[ti] as? TemporaryTurret ?: continue
                if (turret.isDead) continue

                if (CollisionHelper.circleCollision(turret.x, turret.y, turret.radius, bullet.x, bullet.y, bullet.radius)) {
                    turret.takeDamage(bullet.damage)
                    bullet.isDead = true
                    world.remove(bullet, Layer.ENEMY_BULLET)
                    break
                }
            }
        }
    }

    private fun checkEnemyBulletPlayerCollision() {
        val bullets = world.objectsAt(Layer.ENEMY_BULLET)

        for (bi in bullets.lastIndex downTo 0) {
            val bullet = bullets[bi] as? EnemyBullet ?: continue
            if (bullet.isDead) continue

            if (CollisionHelper.circleCollision(player.x, player.y, player.radius, bullet.x, bullet.y, bullet.radius)) {
                player.takeDamage(bullet.damage)
                bullet.isDead = true
                world.remove(bullet, Layer.ENEMY_BULLET)
            }
        }
    }

    private fun checkPlayerCoinCollision() {
        val coins = world.objectsAt(Layer.COIN)

        for (ci in coins.lastIndex downTo 0) {
            val coin = coins[ci] as? Coin ?: continue
            if (coin.isDead) continue

            if (CollisionHelper.circleCollision(player.x, player.y, player.radius + COIN_PICKUP_RANGE_BONUS, coin.x, coin.y, coin.radius)) {
                coin.isDead = true
                GameStats.addGold(coin.value)
                GameSound.playCoin(gctx)
                world.remove(coin, Layer.COIN)
            }
        }
    }

    private fun checkPlayerPickupCollision() {
        val pickups = world.objectsAt(Layer.PICKUP)

        for (pi in pickups.lastIndex downTo 0) {
            val pickup = pickups[pi] as? PickupItem ?: continue
            if (pickup.isDead) continue

            if (CollisionHelper.circleCollision(player.x, player.y, player.radius, pickup.x, pickup.y, pickup.radius)) {
                pickup.isDead = true

                when (pickup.type) {
                    PickupType.MAGNET -> {
                        magnetTime = MAGNET_DURATION
                        GameSound.playPickupMagnet(gctx)
                    }

                    PickupType.HEAL -> {
                        player.heal(HEAL_AMOUNT)
                        GameSound.playPickupHeal(gctx)
                    }
                }

                world.remove(pickup, Layer.PICKUP)
            }
        }
    }

    private fun applyMagnetEffect(gctx: GameContext) {
        val coins = world.objectsAt(Layer.COIN)

        for (i in coins.indices) {
            val coin = coins[i] as? Coin ?: continue
            if (coin.isDead) continue

            val dx = player.x - coin.x
            val dy = player.y - coin.y
            val distanceSq = dx * dx + dy * dy

            if (distanceSq <= MAGNET_RANGE * MAGNET_RANGE) {
                coin.moveToward(player.x, player.y, gctx.frameTime)
            }
        }
    }

    private fun checkEnemyPlayerCollision() {
        val enemies = world.objectsAt(Layer.ENEMY)

        for (ei in enemies.lastIndex downTo 0) {
            val enemy = enemies[ei] as? Enemy ?: continue
            if (enemy.isDead) continue

            val collidedWithPlayer = CollisionHelper.circleCollision(player.x, player.y, player.radius, enemy.x, enemy.y, enemy.radius)
            val reachedBaseLine = enemy.x - enemy.radius <= PLAYER_CONTROL_WIDTH

            if (collidedWithPlayer || reachedBaseLine) {
                player.takeDamage(enemy.collisionDamage)
                world.remove(enemy, Layer.ENEMY)
            }
        }
    }

    private fun checkStageEnd() {
        if (stageEndHandled) return

        val stage = StageManager.currentStage

        if (stage.isBossStage) {
            when {
                bossKilled -> endStage(success = true)
                player.isDead -> endStage(success = false)
                stageTimeLeft <= 0f -> endStage(success = false)
            }
            return
        }

        when {
            player.isDead -> endStage(success = false)
            stageTimeLeft <= 0f -> endStage(success = true)
        }
    }

    private fun endStage(success: Boolean) {
        if (stageEndHandled) return

        stageEndHandled = true
        pendingStageEndSuccess = success
        stageTransitionDelay = 1.15f

        finishTurretDrag()
        touchMode = TouchMode.NONE
        turretPointerId = INVALID_POINTER_ID

        val stage = StageManager.currentStage

        if (success) {
            GameSound.playStageClear(gctx)
            showStageMessage(
                if (stage.isBossStage) "MISSION CLEAR" else "STAGE CLEAR",
                if (stage.isBossStage) "Final Coin ${GameStats.gold}" else "Stage Coin ${GameStats.stageGold}",
                stageTransitionDelay
            )
        } else {
            GameSound.playExplosion(gctx)
            showStageMessage(
                "GAME OVER",
                "Stage Coin ${GameStats.stageGold}",
                stageTransitionDelay
            )
        }
    }

    private fun updateUiMessages(gctx: GameContext) {
        if (stageMessageTime > 0f) {
            stageMessageTime -= gctx.frameTime
            if (stageMessageTime < 0f) stageMessageTime = 0f
        }

        if (installMessageTime > 0f) {
            installMessageTime -= gctx.frameTime
            if (installMessageTime < 0f) installMessageTime = 0f
        }
    }

    private fun showStageMessage(title: String, subTitle: String, duration: Float) {
        stageMessageText = title
        stageMessageSubText = subTitle
        stageMessageTime = duration
    }

    private fun showInstallMessage() {
        installMessageText = "NO ENERGY"
        installMessageTime = 0.75f
        GameSound.playNoEnergy(gctx)
    }

    private fun moveToNextSceneAfterStageEnd() {
        val stage = StageManager.currentStage

        if (!pendingStageEndSuccess) {
            gctx.sceneStack.change(ResultScene(gctx, false))
            return
        }

        if (stage.isBossStage) {
            gctx.sceneStack.change(ResultScene(gctx, true))
        } else {
            gctx.sceneStack.change(StoreScene(gctx))
        }
    }

    private inner class BackgroundObject : IGameObject {

        private val farBg = HorzMirrorScrollBackground(gctx, R.drawable.bg_space_far, 18f)
        private val nearBg = HorzMirrorScrollBackground(gctx, R.drawable.bg_space_near, 46f)

        override fun update(gctx: GameContext) {
            farBg.update(gctx)
            nearBg.update(gctx)
        }

        override fun draw(canvas: Canvas) {
            canvas.drawColor(Color.rgb(6, 8, 18))

            farBg.draw(canvas)
            nearBg.draw(canvas)
        }
    }

    private inner class HudObject : IGameObject {

        private val hpIcon = Sprite(gctx, R.drawable.icon_hp)
        private val energyIcon = Sprite(gctx, R.drawable.icon_energy)
        private val coinIcon = Sprite(gctx, R.drawable.icon_coin)
        private val magnetIcon = Sprite(gctx, R.drawable.icon_magnet)
        private val pauseIcon = Sprite(gctx, R.drawable.btn_pause)

        private val joystickBaseSprite = Sprite(gctx, R.drawable.joystick_base)
        private val joystickKnobSprite = Sprite(gctx, R.drawable.joystick_knob)

        private val hudPanelPaint = Paint().apply {
            color = Color.argb(150, 0, 0, 0)
            isAntiAlias = true
        }

        private val hudStrokePaint = Paint().apply {
            color = Color.argb(120, 150, 210, 255)
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }

        private val hudTitlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 24f
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }

        private val hudSmallPaint = Paint().apply {
            color = Color.argb(230, 220, 235, 255)
            textSize = 20f
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }

        private val hudValuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 22f
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }

        private val centerPanelPaint = Paint().apply {
            color = Color.argb(165, 0, 0, 0)
            isAntiAlias = true
        }

        private val centerStrokePaint = Paint().apply {
            color = Color.argb(170, 130, 220, 255)
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }

        private val stageTitlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 52f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        private val stageSubPaint = Paint().apply {
            color = Color.argb(235, 210, 235, 255)
            textSize = 25f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        private val installToastPaint = Paint().apply {
            color = Color.argb(185, 0, 0, 0)
            isAntiAlias = true
        }

        private val installTextPaint = Paint().apply {
            color = Color.rgb(255, 105, 105)
            textSize = 26f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        init {
            hpIcon.setSize(30f, 30f)
            energyIcon.setSize(30f, 30f)
            coinIcon.setSize(30f, 30f)
            magnetIcon.setSize(30f, 30f)

            pauseIcon.setCenterProportionalWidth(pauseButton.centerX(), pauseButton.centerY(), 92f)

            joystickBaseSprite.setSize(JOYSTICK_BG_RADIUS * 2.15f, JOYSTICK_BG_RADIUS * 2.15f)
            joystickKnobSprite.setSize(JOYSTICK_THUMB_RADIUS * 2.15f, JOYSTICK_THUMB_RADIUS * 2.15f)
        }

        override fun update(gctx: GameContext) {
        }

        private fun drawStageMessage(canvas: Canvas) {
            if (stageMessageTime <= 0f) return

            val centerX = gctx.metrics.width / 2f
            val top = HUD_HEIGHT + 38f
            val panel = RectF(centerX - 360f, top, centerX + 360f, top + 120f)

            canvas.drawRoundRect(panel, 28f, 28f, centerPanelPaint)
            canvas.drawRoundRect(panel, 28f, 28f, centerStrokePaint)

            canvas.drawText(stageMessageText, centerX, top + 55f, stageTitlePaint)

            if (stageMessageSubText.isNotEmpty()) {
                canvas.drawText(stageMessageSubText, centerX, top + 92f, stageSubPaint)
            }
        }

        private fun drawInstallMessage(canvas: Canvas) {
            if (installMessageTime <= 0f) return

            val centerX = gctx.metrics.width / 2f
            val top = HUD_HEIGHT + 175f
            val panel = RectF(centerX - 145f, top, centerX + 145f, top + 54f)

            canvas.drawRoundRect(panel, 22f, 22f, installToastPaint)
            canvas.drawText(installMessageText, centerX, top + 36f, installTextPaint)
        }

        override fun draw(canvas: Canvas) {
            val stage = StageManager.currentStage

            drawHudPanel(canvas)

            canvas.drawText(stage.title, 28f, 30f, hudTitlePaint)
            canvas.drawText("TIME ${stageTimeLeft.toInt()}", 28f, 61f, hudSmallPaint)
            canvas.drawText("KILL $killCount", 135f, 61f, hudSmallPaint)

            hpIcon.setCenter(285f, 34f)
            hpIcon.draw(canvas)
            playerHpGauge.draw(canvas, 325f, 25f, 180f, player.hp.toFloat() / player.maxHp.toFloat())
            canvas.drawText("${player.hp}/${player.maxHp}", 520f, 39f, hudValuePaint)

            energyIcon.setCenter(650f, 34f)
            energyIcon.draw(canvas)
            turretGauge.draw(canvas, 690f, 25f, 180f, turretEnergy / MAX_TURRET_ENERGY)
            canvas.drawText("${turretEnergy.toInt()}/${MAX_TURRET_ENERGY.toInt()}", 885f, 39f, hudValuePaint)

            coinIcon.setCenter(1030f, 34f)
            coinIcon.draw(canvas)
            canvas.drawText("${GameStats.gold}", 1060f, 39f, hudValuePaint)
            canvas.drawText("STAGE ${GameStats.stageGold}", 1060f, 66f, hudSmallPaint)

            canvas.drawText("TURRET ${GameStats.selectedTurretType}", 1235f, 34f, hudSmallPaint)
            canvas.drawText("COST ${getCurrentTurretCost().toInt()}", 1235f, 64f, hudSmallPaint)

            if (magnetTime > 0f) {
                magnetIcon.setCenter(1350f, 58f)
                magnetIcon.draw(canvas)
                magnetGauge.draw(canvas, 1380f, 50f, 80f, magnetTime / MAGNET_DURATION)
            }

            if (stage.isBossStage) {
                canvas.drawText("BOSS ${if (bossKilled) "CLEAR" else "ALIVE"}", 1235f, 86f, hudSmallPaint)
            }

            pauseIcon.setCenter(pauseButton.centerX(), pauseButton.centerY())
            pauseIcon.draw(canvas)

            drawJoystick(canvas)

            drawStageMessage(canvas)
            drawInstallMessage(canvas)
        }

        private fun drawHudPanel(canvas: Canvas) {
            val panel = RectF(12f, 8f, gctx.metrics.width - 12f, HUD_HEIGHT - 8f)
            canvas.drawRoundRect(panel, 18f, 18f, hudPanelPaint)
            canvas.drawRoundRect(panel, 18f, 18f, hudStrokePaint)
        }

        private fun drawJoystick(canvas: Canvas) {
            val centerX = getJoystickCenterX()
            val centerY = getJoystickCenterY()

            joystickBaseSprite.setCenter(centerX, centerY)
            joystickBaseSprite.draw(canvas)

            val thumbX = if (joystickActive) joystickThumbX else centerX
            val thumbY = if (joystickActive) joystickThumbY else centerY

            joystickKnobSprite.setCenter(thumbX, thumbY)
            joystickKnobSprite.draw(canvas)
        }
    }

    companion object {
        const val PLAYER_CONTROL_WIDTH = 0f

        private const val INVALID_POINTER_ID = -1

        private const val HUD_HEIGHT = 84f

        private const val MAX_TURRET_ENERGY = 560f
        private const val BASE_TURRET_COST = 75f
        private const val TURRET_SPACING = 105f
        private const val TURRET_RADIUS = 58f
        private const val TURRET_ENERGY_RECOVER_PER_SEC = 45f

        private const val MID_STAGE_SHOP_INTERVAL = 20f

        private const val LARGE_COIN_DROP_RATE = 25
        private const val MAGNET_DROP_RATE = 9
        private const val HEAL_DROP_RATE = 13

        private const val MAGNET_DURATION = 7f
        private const val MAGNET_RANGE = 900f
        private const val HEAL_AMOUNT = 45

        private const val COIN_PICKUP_RANGE_BONUS = 10f

        private const val JOYSTICK_BG_RADIUS = 95f
        private const val JOYSTICK_THUMB_RADIUS = 42f
        private const val JOYSTICK_TOUCH_RADIUS = 140f
        private const val JOYSTICK_MAX_DISTANCE = 65f
    }
}