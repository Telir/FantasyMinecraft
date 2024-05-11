package by.telir.fantasyminecraft.fantasy.game.property.subproperty

import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType

class ModifyOutDamageProperty(chance: Double) :
    GameProperty(GamePropertyType.MODIFY_OUT_DAMAGE, chance) {
    var percent = 0.0
    var currentHealthPercent = 0.0
    var maxHealthPercent = 0.0
    var amount = 0.0
    var isCrit = false
}