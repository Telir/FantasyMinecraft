package by.telir.fantasyminecraft.fantasy.game.active.subactive

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveState
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.user.User
import java.lang.System.currentTimeMillis
import kotlin.math.max

class ManaRestoringActive(cooldown: Double) : GameActive(cooldown, ActiveType.MANA_RESTORE) {
    var amount: Double = 0.0
    var percent: Double = 0.0
    var healthCost: Double = 0.0

    private var endCooldownTime: Long = 0L

    override var currentCooldown: Double = 0.0
        get() = if (endCooldownTime < currentTimeMillis()) 0.0 else (currentTimeMillis() - endCooldownTime).toDouble() / 1000
        set(value) {
            if (value * 1000 < currentTimeMillis()) {
                endCooldownTime = 0L
                field = 0.0
            } else {
                endCooldownTime = (currentTimeMillis() - value * 1000).toLong()
                field = value
            }
        }

    override fun use(user: User, gameItem: GameItem): ActiveState {
        if (currentCooldown > 0.0) return ActiveState.ON_COOLDOWN

        user.health = max(1.0, user.health - healthCost)
        user.mana += amount + user.attributes[AttributeType.MANA]!!.finalValue * percent

        endCooldownTime = currentTimeMillis() + (cooldown * 1000).toLong()

        return ActiveState.USED
    }
}