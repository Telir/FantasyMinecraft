package by.telir.fantasyminecraft.fantasy.game.listener.restrictions

import by.telir.fantasyminecraft.fantasy.game.item.manager.ItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryInteractEvent

class UntouchableItemEvent : Listener {
    @EventHandler
    fun onInventoryInteract(e: InventoryClickEvent) {
        if (ItemManager(e.currentItem).isUntouchable) e.isCancelled = true
    }

}