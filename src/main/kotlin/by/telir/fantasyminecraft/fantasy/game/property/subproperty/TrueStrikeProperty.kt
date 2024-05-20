package by.telir.fantasyminecraft.fantasy.game.property.subproperty

import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType

class TrueStrikeProperty(chance: Double) :
    GameProperty(PropertyType.TRUE_STRIKE, chance) {
    var damageAmount = 0.0
    var damagePercent = 0.0
    var damageType = DamageType.PHYSICAL
}