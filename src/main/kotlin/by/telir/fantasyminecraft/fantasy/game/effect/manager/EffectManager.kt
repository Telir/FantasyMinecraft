package by.telir.fantasyminecraft.fantasy.game.effect.manager

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.user.User
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.lang.System.currentTimeMillis

class EffectManager(val user: User) {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    private lateinit var stopTask: BukkitTask

    var duration: Double
        get() = if (stopTime < currentTimeMillis()) 0.0 else (stopTime - currentTimeMillis()).toDouble() / 1000
        set(value) {
            if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
            if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

            if (value > 0.0) {
                stopTask.cancel()
                createStopTask(value)
            } else stop()
        }

    private var stopTime: Long = 0
    var effectState = EffectState.NEW

    fun start(duration: Double) {
        Bukkit.broadcastMessage("START!")
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
        if (effectState == EffectState.RUNNING) throw RuntimeException("Effect is Running")

        effectState = EffectState.RUNNING

        createStopTask(duration)
        user.update()
    }

    fun stop() {
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is already Terminated")
        if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

        effectState = EffectState.TERMINATED

        stopTask.cancel()
        user.update()
    }

    private fun createStopTask(duration: Double) {
        stopTime = currentTimeMillis() + (duration * 1000).toLong()
        stopTask = object : BukkitRunnable() {
            override fun run() {
                stop()
            }
        }.runTaskLater(PLUGIN, (duration * 20).toLong())
    }
}