package by.telir.fantasyminecraft.fantasy.game.listener.restrictions

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
        val itemStack = e.item.itemStack

        val gameName = GameItemUtil.getGameName(itemStack) ?: return
        val type = GameItemUtil.getItemType(gameName) ?: return
        val user = UserUtil.find(player.uniqueId)!!

        GameItemManager().add(user, gameName, type, true)
    }
}