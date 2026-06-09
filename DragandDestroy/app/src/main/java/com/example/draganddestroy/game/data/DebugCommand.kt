package com.example.draganddestroy.game.data

object DebugCommand {
    var requestedStageNumber: Int? = null
    var requestedStore = false

    fun requestStage(stageNumber: Int) {
        requestedStageNumber = stageNumber
    }

    fun requestStore() {
        requestedStore = true
    }

    fun consumeStageRequest(): Int? {
        val value = requestedStageNumber
        requestedStageNumber = null
        return value
    }

    fun consumeStoreRequest(): Boolean {
        if (!requestedStore) return false
        requestedStore = false
        return true
    }
}