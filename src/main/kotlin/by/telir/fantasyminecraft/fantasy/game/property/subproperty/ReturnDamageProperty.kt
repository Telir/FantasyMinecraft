package by.telir.fantasyminecraft.fantasy.game.property.subproperty

import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType

class ReturnDamageProperty(chance: Double) :
    GameProperty(PropertyType.RETURN_DAMAGE, chance) {
    var percent = 0.0
    var amount = 0.0
    var isReflect = false
}