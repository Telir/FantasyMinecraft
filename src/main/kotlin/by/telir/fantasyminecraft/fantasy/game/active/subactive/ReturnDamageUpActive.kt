package by.telir.fantasyminecraft.fantasy.game.active.subactive

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.manager.ActiveManager
import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveResult
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.ReturnDamageProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.User

class ReturnDamageUpActive(cooldown: Double) : GameActive(cooldown, ActiveType.RETURN_DAMAGE_UP) {
    lateinit var newProperty: ReturnDamageProperty
    var duration = 0.0

    var manaCost = 0.0
    var healthCost = 0.0

    override fun use(user: User, gameItem: GameItem): ActiveResult {
        if (user.health - healthCost < 0.0) return ActiveResult.NOT_ENOUGH_HEALTH
        if (user.mana - manaCost < 0.0) return ActiveResult.NOT_ENOUGH_MANA
        if (user.getCooldownTime(gameItem) > 0.0) return ActiveResult.ON_COOLDOWN

        user.health -= healthCost
        user.mana -= manaCost

        val activeManager = ActiveManager(gameItem)

        activeManager.changedProperties[PropertyType.RETURN_DAMAGE] = newProperty
        activeManager.start(duration)

        user.addCooldown(gameItem, cooldown)

        return ActiveResult.USED
    }
}