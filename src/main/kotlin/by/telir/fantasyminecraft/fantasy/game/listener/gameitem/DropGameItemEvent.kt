package by.telir.fantasyminecraft.fantasy.game.listener.gameitem

import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.listener.help.InventoryDropInfoEvent
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class DropGameItemEvent : Listener {
    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        if (!InventoryDropInfoEvent.instance.isInventoryDrop) return
        InventoryDropInfoEvent.instance.isInventoryDrop = false
        if (e.isCancelled) return

        val itemStack = e.itemDrop.itemStack
        val gameName = GameItemUtil.getGameName(itemStack) ?: return

        val findGameItem = UserUtil.find(e.player.uniqueId)!!.findGameItem(gameName, itemStack)
        if (findGameItem == null) {
            e.itemDrop.remove()
            return
        }

        if (e.player.gameMode == GameMode.CREATIVE) {
            e.player.sendMessage("You are in creative")
            return
        }

        GameItemManager().remove(UserUtil.find(e.player.uniqueId)!!, itemStack, true)
    }
}