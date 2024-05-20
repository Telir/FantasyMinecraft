package by.telir.fantasyminecraft.fantasy.game.active.subactive

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveResult
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.user.User
import kotlin.math.max

class ManaRestoringActive(cooldown: Double) : GameActive(cooldown, ActiveType.MANA_RESTORE) {
    var amount: Double = 0.0
    var percent: Double = 0.0
    var healthCost: Double = 0.0

    override fun use(user: User, gameItem: GameItem): ActiveResult {
        if (user.getCooldownTime(gameItem) > 0.0) return ActiveResult.ON_COOLDOWN

        user.health = max(1.0, user.health - healthCost)
        user.mana += amount + user.attributes[AttributeType.MANA]!!.finalValue * percent

        user.addCooldown(gameItem, cooldown)

        return ActiveResult.USED
    }
}