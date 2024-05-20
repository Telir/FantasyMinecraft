package by.telir.fantasyminecraft.fantasy.game.property.subproperty

import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType

class HitEffectProperty(chance: Double) :
    GameProperty(PropertyType.HIT_EFFECT, chance) {
    lateinit var effect: Effect
}