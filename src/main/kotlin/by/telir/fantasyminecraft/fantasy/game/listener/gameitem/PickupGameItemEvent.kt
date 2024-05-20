package by.telir.fantasyminecraft.fantasy.game.listener.gameitem

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class PickupGameItemEvent : Listener {
    private val plugin = FantasyMinecraft.instance
    @EventHandler
    fun onEntityPickupItem(e: EntityPickupItemEvent) {
        val player = e.entity as? Player ?: return
        val item = e.item
        val itemStack = item.itemStack

        val gameItem = plugin.droppedGameItems[itemStack]
        if (gameItem == null) {
            item.remove()
            return
        }

        val user = UserUtil.find(player.uniqueId)!!

        GameItemManager().pickup(user, gameItem, itemStack)
    }
}