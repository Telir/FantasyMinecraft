package by.telir.fantasyminecraft.fantasy.game.effect

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.effect.status.EffectState
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import org.bukkit.entity.LivingEntity

abstract class Effect(val type: EffectType) {
    abstract val state: EffectState
    abstract var currentDuration: Double
    val attributeChanges = mutableMapOf<AttributeType, AttributeModifier>()

    abstract fun start(livingEntity: LivingEntity)
    abstract fun forceStop()
}