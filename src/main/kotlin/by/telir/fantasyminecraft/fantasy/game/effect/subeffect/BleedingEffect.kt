package by.telir.fantasyminecraft.fantasy.game.effect.subeffect

import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.manager.PeriodEffectManager
import by.telir.fantasyminecraft.fantasy.game.effect.manager.PeriodEffectManager.PeriodType
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import by.telir.fantasyminecraft.fantasy.game.user.User

class BleedingEffect(user: User, private val duration: Double, private val period: Double) :
    Effect(EffectType.BLEEDING) {
    var amount = 0.0
    var percent = 0.0

    private lateinit var periodEffectManager: PeriodEffectManager

    override fun start(user: User) {
        periodEffectManager = PeriodEffectManager(user, PeriodType.CHANGE_HEALTH)
        periodEffectManager.amount = -amount
        periodEffectManager.percent = -percent

        periodEffectManager.start(duration, period)
    }

    override fun forceStop() {
        periodEffectManager.stop()
    }


    override val state: EffectState
        get() = periodEffectManager.effectState

    override var currentDuration: Double
        get() = periodEffectManager.duration
        set(value) {
            periodEffectManager.duration = value
        }
}