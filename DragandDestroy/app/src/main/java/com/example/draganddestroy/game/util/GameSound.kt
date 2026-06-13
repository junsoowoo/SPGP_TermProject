package com.example.draganddestroy.game.util

import android.os.SystemClock
import com.example.draganddestroy.R
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

object GameSound {

    private var bgmStarted = false

    private var lastPlayerShotTime = 0L
    private var lastTurretBasicTime = 0L
    private var lastTurretRapidTime = 0L
    private var lastEnemyHitTime = 0L
    private var lastCoinTime = 0L
    private var lastNoEnergyTime = 0L

    fun startBgm(gctx: GameContext) {
        if (bgmStarted) return

        bgmStarted = true
        gctx.res.sound.playMusic(R.raw.bgm_space_loop)
    }

    fun restartBgm(gctx: GameContext) {
        bgmStarted = true
        gctx.res.sound.playMusic(R.raw.bgm_space_loop)
    }

    fun stopBgm(gctx: GameContext) {
        if (!bgmStarted) return

        bgmStarted = false
        gctx.res.sound.stopMusic()
    }

    fun playPlayerShot(gctx: GameContext) {
        if (!canPlayPlayerShot()) return
        gctx.res.sound.playEffect(R.raw.sfx_player_shot)
    }

    fun playTurretBasic(gctx: GameContext) {
        if (!canPlayTurretBasic()) return
        gctx.res.sound.playEffect(R.raw.sfx_turret_basic)
    }

    fun playTurretRapid(gctx: GameContext) {
        if (!canPlayTurretRapid()) return
        gctx.res.sound.playEffect(R.raw.sfx_turret_rapid)
    }

    fun playEnemyHit(gctx: GameContext) {
        if (!canPlayEnemyHit()) return
        gctx.res.sound.playEffect(R.raw.sfx_enemy_hit)
    }

    fun playExplosion(gctx: GameContext) {
        gctx.res.sound.playEffect(R.raw.sfx_explosion)
    }

    fun playCoin(gctx: GameContext) {
        if (!canPlayCoin()) return
        gctx.res.sound.playEffect(R.raw.sfx_coin)
    }

    fun playNoEnergy(gctx: GameContext) {
        if (!canPlayNoEnergy()) return
        gctx.res.sound.playEffect(R.raw.sfx_no_energy)
    }

    fun playStageStart(gctx: GameContext) {
        gctx.res.sound.playEffect(R.raw.sfx_stage_start)
    }

    fun playStageClear(gctx: GameContext) {
        gctx.res.sound.playEffect(R.raw.sfx_stage_clear)
    }

    private fun canPlayPlayerShot(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastPlayerShotTime < 45L) return false
        lastPlayerShotTime = now
        return true
    }

    private fun canPlayTurretBasic(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastTurretBasicTime < 90L) return false
        lastTurretBasicTime = now
        return true
    }

    private fun canPlayTurretRapid(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastTurretRapidTime < 45L) return false
        lastTurretRapidTime = now
        return true
    }

    private fun canPlayEnemyHit(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastEnemyHitTime < 35L) return false
        lastEnemyHitTime = now
        return true
    }

    private fun canPlayCoin(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastCoinTime < 45L) return false
        lastCoinTime = now
        return true
    }

    private fun canPlayNoEnergy(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - lastNoEnergyTime < 180L) return false
        lastNoEnergyTime = now
        return true
    }
}