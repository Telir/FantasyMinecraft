package by.telir.fantasyminecraft.fantasy.game.active.subactive

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveResult
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.user.User

class HealthRestoreActive(cooldown: Double) : GameActive(cooldown, ActiveType.HEALTH_RESTORE) {
    var amount = 0.0
    var percent = 0.0
    var manaCost = 0.0

    override fun use(user: User, gameItem: GameItem): ActiveResult {
        if (user.mana - manaCost < 0) return ActiveResult.NOT_ENOUGH_MANA
        if (user.getCooldownTime(gameItem) > 0.0) return ActiveResult.ON_COOLDOWN

        user.mana -= manaCost
        user.health += amount + user.attributes[AttributeType.HEALTH]!!.finalValue * percent
        user.addCooldown(gameItem, cooldown)

        return ActiveResult.USED
    }
}