package by.telir.fantasyminecraft.fantasy.game.property.subproperty

import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType

class LifestealProperty(chance: Double) :
    GameProperty(PropertyType.LIFESTEAL, chance) {
    var percent = 0.0
    var enemyHealthPercent = 0.0
    var enemyMaxHealthPercent = 0.0
    var amount = 0.0
    var amplifier = 0.0
    var damageType = DamageType.PHYSICAL
    var damagePercent = 0.0
}