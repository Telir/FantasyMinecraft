package by.telir.fantasyminecraft.fantasy.game.active

import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveResult
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.user.User

abstract class GameActive(protected val cooldown: Double, val activeType: ActiveType) {
    abstract fun use(user: User, gameItem: GameItem): ActiveResult
}