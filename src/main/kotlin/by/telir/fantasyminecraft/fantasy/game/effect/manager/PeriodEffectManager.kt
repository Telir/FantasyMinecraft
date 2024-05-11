package by.telir.fantasyminecraft.fantasy.game.effect.manager

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.user.User
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.lang.System.currentTimeMillis
import kotlin.properties.Delegates

class PeriodEffectManager(val user: User, val type: PeriodType) {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    private lateinit var periodTask: BukkitTask

    var duration: Double
        get() = if (stopTime < currentTimeMillis()) 0.0 else (currentTimeMillis() - stopTime).toDouble() / 1000
        set(value) {
            if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
            if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

            if (value > 0.0) {
                periodTask.cancel()
                createPeriodTask(value)
            } else stop()
        }
    private var period: Double by Delegates.notNull()

    private var stopTime: Long = 0
    var effectState = EffectState.NEW

    var amount = 0.0
    var percent = 0.0

    fun start(duration: Double, period: Double) {
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
        if (effectState == EffectState.RUNNING) throw RuntimeException("Effect is Running")

        this.period = period
        effectState = EffectState.RUNNING

        createPeriodTask(duration)
        user.update()
    }

    fun stop() {
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is already Terminated")
        if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

        effectState = EffectState.TERMINATED

        periodTask.cancel()
        user.update()
    }

    private fun createPeriodTask(duration: Double) {
        stopTime = currentTimeMillis() + (duration * 1000).toLong()
        val maxIterations = (duration / period).toLong()
        var iterations = 0L

        periodTask = object : BukkitRunnable() {
            override fun run() {
                if (currentTimeMillis() > stopTime) stop()
                when (type) {
                    PeriodType.CHANGE_MANA -> {
                        user.mana += amount
                        user.mana += user.mana * percent
                    }

                    PeriodType.CHANGE_HEALTH -> {
                        user.health += amount
                        user.health += user.health * percent
                    }
                }
                iterations++
                if (maxIterations == iterations) this.cancel()

            }
        }.runTaskTimerAsynchronously(PLUGIN, 0, (period * 20).toLong())
    }

    enum class PeriodType {
        CHANGE_MANA,
        CHANGE_HEALTH;
    }
}