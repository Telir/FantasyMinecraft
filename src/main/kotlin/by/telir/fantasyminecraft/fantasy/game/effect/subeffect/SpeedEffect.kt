package by.telir.fantasyminecraft.fantasy.game.effect.subeffect

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.manager.EffectManager
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import by.telir.fantasyminecraft.fantasy.game.user.User

class SpeedEffect(private val duration: Double) : Effect(EffectType.SPEED) {
    var amount = 0.0
    var value = 0.0

    private lateinit var effectManager: EffectManager
    private var user: User? = null

    override fun start(user: User) {
        this.user = user
        effectManager = EffectManager(user)

        if (amount != 0.0) attributeChanges[AttributeType.MOVEMENT_SPEED] =
            AttributeModifier("speedEffect", amount, OperationType.ADD, false)

        if (value != 0.0) attributeChanges[AttributeType.MOVEMENT_SPEED] =
            AttributeModifier("speedEffect", value, OperationType.SCALAR, false)

        effectManager.start(duration)
    }

    override fun forceStop() {
        if (user == null) return
        if (user!!.effects[type] == null) return
        user!!.effects[type]?.remove(this)
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