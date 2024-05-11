package by.telir.fantasyminecraft.fantasy.game.active.subactive

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.manager.ActiveManager
import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveState
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.ReturnDamageProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType
import by.telir.fantasyminecraft.fantasy.game.user.User

class ReturnDamageUpActive(cooldown: Double) : GameActive(cooldown, ActiveType.RETURN_DAMAGE_UP) {
    lateinit var newProperty: ReturnDamageProperty
    var duration: Double = 0.0

    var manaCost: Double = 0.0
    var healthCost: Double = 0.0

    private lateinit var activeManager: ActiveManager

    override var currentCooldown: Double
        get() = activeManager.cooldown
        set(value) {
            activeManager.cooldown = value
        }


    override fun use(user: User, gameItem: GameItem): ActiveState {
        if (user.health - healthCost < 0.0) return ActiveState.NOT_ENOUGH_HEALTH
        if (user.mana - manaCost < 0.0) return ActiveState.NOT_ENOUGH_MANA
        if (currentCooldown > 0.0) return ActiveState.ON_COOLDOWN

        user.health -= healthCost
        user.mana -= manaCost

        val activeManager = ActiveManager(gameItem)

        activeManager.changedProperties[GamePropertyType.RETURN_DAMAGE] = newProperty
        activeManager.start(duration)

        return ActiveState.USED
    }
}