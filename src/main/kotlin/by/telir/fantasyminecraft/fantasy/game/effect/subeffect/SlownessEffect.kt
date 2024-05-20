package by.telir.fantasyminecraft.fantasy.game.effect.subeffect

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.manager.CreateEffectManager
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import by.telir.fantasyminecraft.fantasy.game.user.User
import org.bukkit.entity.LivingEntity

class SlownessEffect(private val duration: Double) : Effect(EffectType.SLOWNESS) {
    var amount = 0.0
    var value = 0.0

    private lateinit var createEffectManager: CreateEffectManager
    private var user: User? = null

    override fun start(livingEntity: LivingEntity) {
        createEffectManager = CreateEffectManager(livingEntity, this)

        if (amount != 0.0) attributeChanges[AttributeType.MOVEMENT_SPEED] =
            AttributeModifier("slownessEffect", -amount, OperationType.ADD, false)

        if (value != 0.0) attributeChanges[AttributeType.MOVEMENT_SPEED] =
            AttributeModifier("slownessEffect", (1 - value), OperationType.SCALAR, false)

        createEffectManager.start(duration)
    }

    override fun forceStop() {
        if (user == null) return
        if (user!!.effects[type] == null) return
        user!!.effects[type]?.remove(this)
        createEffectManager.stop()
    }

    override val state: EffectState
        get() = createEffectManager.effectState

    override var currentDuration: Double
        get() = createEffectManager.duration
        set(value) {
            createEffectManager.duration = value
        }
}