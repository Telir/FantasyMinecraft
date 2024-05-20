package by.telir.fantasyminecraft.fantasy.game.effect.manager

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.attribute.type.MinecraftAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.util.AttributeUtil
import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType.EffectActionType
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType.PeriodEffectType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.lang.System.currentTimeMillis
import kotlin.properties.Delegates

class CreateEffectManager(private val livingEntity: LivingEntity, private val effect: Effect) {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    private val actionType = effect.type.actionType

    private var stopTime: Long = 0
    var effectState = EffectState.NEW

    private lateinit var task: BukkitTask

    var period: Double by Delegates.notNull()
    private val periodType = effect.type.periodType

    var amount: Double = 0.0
    var percent: Double = 0.0

    var duration: Double
        get() = if (stopTime < currentTimeMillis()) 0.0 else (currentTimeMillis() - stopTime).toDouble() / 1000
        set(value) {
            if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
            if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

            if (value > 0.0) {
                task.cancel()
                createTask(livingEntity, value)
            } else stop()
        }


    private fun createTask(livingEntity: LivingEntity, duration: Double) {
        stopTime = currentTimeMillis() + (duration * 1000).toLong()
        if (actionType == EffectActionType.DEFAULT) {
            task = object : BukkitRunnable() {
                override fun run() {
                    stop()
                }
            }.runTaskLater(PLUGIN, (duration * 20).toLong())
        }

        if (actionType == EffectActionType.PERIOD) {
            val maxIterations = (duration / period).toLong()
            var iterations = 0L

            task = object : BukkitRunnable() {
                override fun run() {
                    if (currentTimeMillis() > stopTime) stop()
                    when (periodType) {
                        PeriodEffectType.CHANGE_MANA -> {
                            val user = UserUtil.find(livingEntity.uniqueId)
                            if (user != null) {
                                user.mana += amount
                                user.mana += user.mana * percent
                            }
                        }

                        PeriodEffectType.CHANGE_HEALTH -> {
                            livingEntity.health += amount
                            livingEntity.health += AttributeUtil.getValue(
                                livingEntity,
                                MinecraftAttribute.GENERIC_MAX_HEALTH
                            ) * percent
                        }

                        null -> {}
                    }
                    iterations++
                    if (maxIterations == iterations) stop()

                }
            }.runTaskTimerAsynchronously(PLUGIN, (period * 20).toLong(), (period * 20).toLong())
        }
    }

    fun start(duration: Double) {
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is Terminated")
        if (effectState == EffectState.RUNNING) throw RuntimeException("Effect is already Running")

        effectState = EffectState.RUNNING

        StartEffectManager(livingEntity, effect).startEffect()
        createTask(livingEntity, duration)
    }

    fun stop() {
        if (effectState == EffectState.TERMINATED) throw RuntimeException("Effect is already Terminated")
        if (effectState != EffectState.RUNNING) throw RuntimeException("Effect is not Running")

        effectState = EffectState.TERMINATED

        StartEffectManager(livingEntity, effect).stopEffect()
        task.cancel()
    }
}