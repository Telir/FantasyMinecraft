package by.telir.fantasyminecraft.fantasy.game.effect.subeffect

import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.manager.CreateEffectManager
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import org.bukkit.entity.LivingEntity

class BleedingEffect(private val duration: Double, private val period: Double) : Effect(EffectType.BLEEDING) {
    var amount = 0.0
    var value = 0.0

    private lateinit var effectManager: CreateEffectManager

    override fun start(livingEntity: LivingEntity) {
        effectManager = CreateEffectManager(livingEntity, this)
        effectManager.amount = -amount
        effectManager.percent = -value
        effectManager.period = period

        effectManager.start(duration)
    }

    override fun forceStop() {
        effectManager.stop()
    }


    override val state: EffectState
        get() = effectManager.effectState

    override var currentDuration: Double
        get() = effectManager.duration
        set(value) {
            effectManager.duration = value
        }
}