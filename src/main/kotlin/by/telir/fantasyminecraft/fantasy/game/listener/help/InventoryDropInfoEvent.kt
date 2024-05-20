package by.telir.fantasyminecraft.fantasy.game.listener.help

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryDropInfoEvent : Listener {
    companion object {
        lateinit var instance: InventoryDropInfoEvent
    }

    init {
        instance = this
    }

    var isInventoryDrop = false

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        instance = this
        val dropActions = setOf(
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ONE_SLOT
        )

        isInventoryDrop = dropActions.contains(e.action)
    }
}