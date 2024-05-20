package by.telir.fantasyminecraft.fantasy.game.listener.gameitem

import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class PickupGameItemEvent : Listener {
    @EventHandler
    fun onEntityPickupItem(e: EntityPickupItemEvent) {
        val player = e.entity as? Player ?: return
        val item = e.item
        val itemStack = item.itemStack

        val gameName = GameItemUtil.getGameName(itemStack) ?: return
        val itemType = GameItemUtil.getItemType(gameName) ?: return

        val user = UserUtil.find(player.uniqueId)!!

        for (i in 1..itemStack.amount) {
            GameItemManager().add(user, gameName, itemType, true)
        }
    }
}