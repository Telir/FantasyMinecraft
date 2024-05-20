package by.telir.fantasyminecraft.fantasy.game.listener.restrictions

import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.listener.help.InventoryDropInfoEvent
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class DropGameItemEvent : Listener {
    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        if (e.player.gameMode == GameMode.CREATIVE) {
            e.player.sendMessage("You are in creative")
            return
        }
        if (!InventoryDropInfoEvent.instance.isInventoryDrop) return

        val itemStack = e.itemDrop.itemStack
        if (!GameItemUtil.isGameItem(itemStack)) return

        GameItemManager().remove(UserUtil.find(e.player.uniqueId)!!, itemStack, true)
    }
}